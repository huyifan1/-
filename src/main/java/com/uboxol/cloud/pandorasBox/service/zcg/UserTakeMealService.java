package com.uboxol.cloud.pandorasBox.service.zcg;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uboxol.cloud.pandorasBox.api.req.Grid;
import com.uboxol.cloud.pandorasBox.api.res.EntryResult;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.OrderRec;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRecRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRepository;
import lombok.extern.slf4j.Slf4j;
/*
 * 用户确认取餐接口
 * 
	用户确认取餐接口
	输入：业务渠道id，柜子id,格子id，确认取餐
	输出：订单id，业务方id，柜子id，格子id，格子状态：空闲中，
	  订单状态：订单生成，
	  订单生成时间，
	  订单状态：订单确认，
	  订单确认时间，
	  订单状态：订单完成，
	  订单完成时间
	  下发开格指令
  */
@Slf4j
@Service
public class UserTakeMealService  extends HttpProvider {
	private final CounterRepository counterRepository;
	private final OrderRepository orderRepository; 
	private final OrderRecRepository orderRecRepository; 
	private static final String PATH = "http://boxcloud.uboxol.com/api/open_box?";
	private String v = "1.0";
    private String t = "" + System.currentTimeMillis() / 1000;
    private String app_id = "ubox_20150820";
    private String app_secret = "PzUqPozMduUc6RohPxVdPmtXNfzzL6";
    
	@Autowired 
	 public UserTakeMealService(final CounterRepository counterRepository,final OrderRepository orderRepository,final OrderRecRepository orderRecRepository) {
		 	 this.counterRepository = counterRepository;
			 this.orderRepository = orderRepository;
			 this.orderRecRepository = orderRecRepository;
	 }

	public EntryResult takeMeal(Grid req) { 
		EntryResult entryResult = new EntryResult();
		OrderInformation orderInformation = null;
		try {
			String cabinetId= req.getCabinetId();
			String gridId= req.getGridId();
			Counter counter = counterRepository.findByCabinetIdAndGridId(cabinetId,gridId);
			if(counter==null) throw new RuntimeException("counter表中没有对应的格子");
			
			//下发开格指令
			Map postmap = new HashMap();
			postmap.clear();
	        postmap.put("inner_code", cabinetId);
	        postmap.put("unit_code", gridId);
	        postmap.put("unit_type", "1");
	        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
	        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
	        HttpResponse response = httpClient.execute(httpPost);
	        String value = consumeResponse(response.getEntity());
	        if(value.equals("1")) {
	        	logger.info("柜子："+cabinetId+"格口"+gridId+"用户取餐下发开格指令发送成功");
	        	
	        	counter.setGridCurStatus(3);
				counter.setOrderCurStatus(3);
				counter.setCurTime(new Timestamp(new Date().getTime()));
				counterRepository.saveAndFlush(counter);
				logger.info("更新格子状态为空闲中");
	        	
				Order order =orderRepository.findByOrderId(counter.getCurOrderId());
				if(order==null) throw new RuntimeException("order表中没有对应的订单");
				order.setGridStatus(3);
				order.setOrderStatus(3);
				order.setOrderCompleteTime(new Timestamp(new Date().getTime()));
				orderRepository.saveAndFlush(order);
				logger.info("更新订单状态为订单完成");
				
				//添加order_rec表的记录流水单子
				OrderRec orderRec = new OrderRec();
				orderRec.setOrderId(order.getOrderId());
				orderRec.setBussinessId(order.getBussinessId());
				orderRec.setCabinetId(counter.getCabinetId());
				orderRec.setGridId(counter.getGridId());
				orderRec.setSpecs(counter.getSpecs());
				orderRec.setBranchCompany(counter.getBranchCompany());
				orderRec.setPointId(counter.getPointId());
				orderRec.setPointName(counter.getPointName());
				orderRec.setOrderReason("用户取餐");
				orderRec.setCleanStatus(0);
				orderRec.setGridStatus(3);
				orderRec.setOrderStatus(3);
				orderRec.setOrderCompleteTime(new Timestamp(new Date().getTime()));
				orderRecRepository.saveAndFlush(orderRec);
				logger.info("orderRec记录表新增完毕");
				
				orderInformation = new OrderInformation();
				orderInformation.setOrderId(order.getOrderId());
				orderInformation.setBussinessId(order.getBussinessId());
				orderInformation.setCabinetId(order.getCabinetId());
				orderInformation.setGridId(order.getGridId());
				orderInformation.setGridStatus(3);
				orderInformation.setOrderStatus(3);
				orderInformation.setOrderTime(order.getOrderTime());
				orderInformation.setOrderConfirmTime(order.getOrderConfirmTime());
				orderInformation.setOrderCompleteTime(order.getOrderCompleteTime());
				
	        	entryResult.setCode("200");
				entryResult.setMsg("用户取餐成功");
				entryResult.setOrderInformation(orderInformation);
	        }else {
	        	logger.info("柜子："+cabinetId+"格口"+gridId+"用户取餐下发开格指令失败"+value+"(1-发送成功 0-发送失败 2-离线 -1-售货机不支持)");
	        	entryResult.setCode("500");
				entryResult.setMsg("下发开格指令失败");
				entryResult.setOrderInformation(orderInformation);
	        }
			
		}catch (Exception e) {
			logger.error("用户确认取餐接口出错:{}", e.getMessage(), e); 
		}
		return entryResult;
	}
}
