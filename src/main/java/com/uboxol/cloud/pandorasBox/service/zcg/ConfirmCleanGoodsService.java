package com.uboxol.cloud.pandorasBox.service.zcg;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.uboxol.budd.common.util.HttpUtils;
import com.uboxol.budd.common.util.JsonUtils;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.api.res.ClearResult;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.CleanRecord;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.OrderRec;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CleanRecordRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRecRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * 清货通知、清货确认接口（批量开格，0214补充）
 * 
	清货通知：建议由渠道方通知给中台，即渠道订单时长满足渠道方的清货条件后，通知到中台，中台再通知到运营
	输入：柜子id.格子id（从清货效率考虑，偏向单个柜子id下的批量格子id），清货开格
	输出：各订单id，业务方id，柜子id，格子id，格子状态：空闲中，
           订单状态：订单生成，
           订单生成时间，
           订单状态：订单确认，
           订单确认时间，
           订单状态：订单关闭，
           订单关闭时间
           下发开格指令
*/

@Slf4j
@Service
public class ConfirmCleanGoodsService extends HttpProvider {
	private final CounterRepository counterRepository;
	private final OrderRepository orderRepository; 
	private final OrderRecRepository orderRecRepository; 
	private final CleanRecordRepository cleanRecordRepository;
	private static final String PATH = "http://boxcloud.uboxol.com/api/open_box?";
	private String v = "1.0";
    private String t = "" + System.currentTimeMillis() / 1000;
    private String app_id = "ubox_20150820";
    private String app_secret = "PzUqPozMduUc6RohPxVdPmtXNfzzL6";
    
	
	@Autowired 
	 public ConfirmCleanGoodsService(final CounterRepository counterRepository,final OrderRepository orderRepository,final OrderRecRepository orderRecRepository,CleanRecordRepository cleanRecordRepository) {
	 	 this.counterRepository = counterRepository;
		 this.orderRepository = orderRepository;
		 this.orderRecRepository = orderRecRepository;
		 this.cleanRecordRepository = cleanRecordRepository;
 }
	
	public ClearResult confirmCleaning(ClearGrids req) {
		ClearResult clearResult = new ClearResult();
		List<OrderInformation> list = new ArrayList<OrderInformation>();
		try {
			String cabinetId = req.getCabinetId();
			List<String> gridIds = req.getGridIds();
			for(String gridId : gridIds) {
				OrderInformation o = clear(cabinetId,gridId);
				if(o!=null) {
					list.add(o);
				}
			}
			
			if(list.size()>0) {
				//运维清货成功后调此接口，单独http post请求，json格式 通知业务方
				String json = "{\"list\":"+JsonUtils.toString(list)+"}";
				String response = HttpUtils.postJson("http://ybklocker.uboxol.com/cabinet/cleanGoods/", json);
				 JSONObject result = JSONObject.parseObject(response);
				if(result.get("error_code").toString().equals("0")) {
					logger.info("通知业务方，保洁/清货通知成功");
				}else {
					logger.info("通知业务方，保洁/清货通知失败。error_code："+result.get("error_code").toString());
				}
			}
			
			clearResult.setCode("200");
			clearResult.setMsg("保洁/清货通知成功");
			clearResult.setList(list);
			
		}catch (Exception e) {
			logger.error("清货通知接口出错:{}", e.getMessage(), e); 
		}
		return clearResult;
	}
	
	public OrderInformation clear(String cabinetId,String gridId) { 
		OrderInformation orderInformation = null;
		try {
			Counter counter = counterRepository.findByCabinetIdAndGridId(cabinetId, gridId);
			if(counter==null) throw new RuntimeException("counter表中没有对应的格子");
			int gridStatus = counter.getGridCurStatus();
			
			if(gridStatus==1 || gridStatus==2) {
				logger.info("柜子："+cabinetId+"格口"+gridId+"交互中或占用中，不可开格");
				
				Order order =orderRepository.findByOrderId(counter.getCurOrderId());
				if(order!=null) {
					orderInformation = new OrderInformation();
					orderInformation.setOrderId(order.getOrderId());
					orderInformation.setBussinessId(order.getBussinessId());
					orderInformation.setCabinetId(order.getCabinetId());
					orderInformation.setGridId(order.getGridId());
					orderInformation.setGridStatus(order.getGridStatus());
					orderInformation.setOrderStatus(order.getOrderStatus());
					orderInformation.setOrderTime(order.getOrderTime());
					orderInformation.setOrderConfirmTime(order.getOrderConfirmTime());
					orderInformation.setOrderCompleteTime(order.getOrderCompleteTime());
					orderInformation.setOrderClosedTime(order.getOrderClosedTime());
					logger.info("返回OrderInformation"+orderInformation);
				} 
			}
			
			if(gridStatus==3) {
				logger.info("与订单无关，纯粹保洁开格并下发开格指令");
				//下发开格指令
				Map postmap = new HashMap();
				postmap.clear();
		        postmap.put("inner_code", cabinetId);
		        postmap.put("unitCode", gridId);
		        postmap.put("unitType", "1");
		        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
		        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
		        HttpResponse response = httpClient.execute(httpPost);
		        String value = consumeResponse(response.getEntity());
		        if(value.equals("1")) {
		        	logger.info("柜子："+cabinetId+"格口"+gridId+"下发开格指令发送成功");
					CleanRecord cleanRecord = new CleanRecord();
					cleanRecord.setCabinetId(cabinetId);
					cleanRecord.setGridId(gridId);
					cleanRecord.setCleanTime(new Timestamp(new Date().getTime()));
					cleanRecordRepository.saveAndFlush(cleanRecord);
					logger.info("clean_record保洁新增完毕");
					
					Order order =orderRepository.findByOrderId(counter.getCurOrderId());
					if(order!=null) {
						orderInformation = new OrderInformation();
						orderInformation.setOrderId(order.getOrderId());
						orderInformation.setBussinessId(order.getBussinessId());
						orderInformation.setCabinetId(order.getCabinetId());
						orderInformation.setGridId(order.getGridId());
						orderInformation.setGridStatus(order.getGridStatus());
						orderInformation.setOrderStatus(order.getOrderStatus());
						orderInformation.setOrderTime(order.getOrderTime());
						orderInformation.setOrderConfirmTime(order.getOrderConfirmTime());
						orderInformation.setOrderCompleteTime(order.getOrderCompleteTime());
						orderInformation.setOrderClosedTime(order.getOrderClosedTime());
						logger.info("返回OrderInformation"+orderInformation);
					} 
					
		        }else {
		        	logger.info("柜子："+cabinetId+"格口"+gridId+"下发开格指令失败"+value+"(1-发送成功 0-发送失败 2-离线 -1-售货机不支持)");
		        }
			}
			
			if(gridStatus==4) {
				//下发开格指令
				Map postmap = new HashMap();
				postmap.clear();
		        postmap.put("inner_code", cabinetId);
		        postmap.put("unitCode", gridId);
		        postmap.put("unitType", "1");
		        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
		        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
		        HttpResponse response = httpClient.execute(httpPost);
		        String value = consumeResponse(response.getEntity());
		        if(value.equals("1")) {
		        	logger.info("柜子："+cabinetId+"格口"+gridId+"下发开格指令发送成功");
		        	logger.info("正常清货开格，修改订单状态为【4订单关闭】，清货状态为【2已清货】，格口状态为【3空闲中】");
					counter.setCurTime(new Timestamp(new Date().getTime()));
					counter.setCleanStatus(2);
					counter.setGridCurStatus(3);
					counter.setOrderCurStatus(4);
					counter.setCleanTime(new Timestamp(new Date().getTime()));
					counterRepository.saveAndFlush(counter);
					logger.info("更新格子状态完毕");
		        	
					Order order =orderRepository.findByOrderId(counter.getCurOrderId());
					if(order==null) throw new RuntimeException("order表中没有对应的订单");
					order.setCleanStatus(2);
					order.setGridStatus(3);
					order.setOrderStatus(4);
					order.setOrderClosedTime(new Timestamp(new Date().getTime()));
					orderRepository.saveAndFlush(order);
					logger.info("更新订单状态完毕");
					
					OrderRec orderRec = new OrderRec();
					orderRec.setOrderId(order.getOrderId());
					orderRec.setBussinessId(order.getBussinessId());
					orderRec.setCabinetId(counter.getCabinetId());
					orderRec.setGridId(counter.getGridId());
					orderRec.setSpecs(counter.getSpecs());
					orderRec.setBranchCompany(counter.getBranchCompany());
					orderRec.setPointId(counter.getPointId());
					orderRec.setPointName(counter.getPointName());
					orderRec.setOrderReason("清货确认");
					orderRec.setCleanStatus(2);
					orderRec.setGridStatus(3);
					orderRec.setOrderStatus(4);
					orderRec.setOrderClosedTime(new Timestamp(new Date().getTime()));
					orderRec.setOrderReason("清货确认");
					orderRecRepository.saveAndFlush(orderRec);
					logger.info("orderRec记录表新增完毕");
					
					logger.info("clean_record清货记录新增完毕");
					CleanRecord cleanRecord = new CleanRecord();
					cleanRecord.setCabinetId(cabinetId);
					cleanRecord.setGridId(gridId);
					cleanRecord.setCurOrderId(order.getOrderId());
					cleanRecord.setCleanTime(new Timestamp(new Date().getTime()));
					cleanRecordRepository.saveAndFlush(cleanRecord);
					
					orderInformation = new OrderInformation();
					orderInformation.setOrderId(order.getOrderId());
					orderInformation.setBussinessId(order.getBussinessId());
					orderInformation.setCabinetId(order.getCabinetId());
					orderInformation.setGridId(order.getGridId());
					orderInformation.setGridStatus(order.getGridStatus());
					orderInformation.setOrderStatus(order.getOrderStatus());
					orderInformation.setOrderTime(order.getOrderTime());
					orderInformation.setOrderConfirmTime(order.getOrderConfirmTime());
					orderInformation.setOrderCompleteTime(order.getOrderCompleteTime());
					orderInformation.setOrderClosedTime(order.getOrderClosedTime());
					logger.info("返回OrderInformation"+orderInformation);
		        	
		        }else {
		        	logger.info("柜子："+cabinetId+"格口"+gridId+"下发开格指令失败"+value+"(1-发送成功 0-发送失败 2-离线 -1-售货机不支持)");
		        }
		        
			}
			
		}catch (Exception e) {
			logger.error("保洁/清货接口出错:{}", e.getMessage(), e); 
		}
		return orderInformation;
	}
	
}