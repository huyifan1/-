package com.uboxol.cloud.pandorasBox.service.zcg;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.druid.util.StringUtils;
import com.uboxol.cloud.pandorasBox.api.req.RiderPutMeal;
import com.uboxol.cloud.pandorasBox.api.res.EntryResult;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.OrderRec;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRecRepository;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.OrderRepository;
import com.uboxol.cloud.pandorasBox.utils.HyfUtils;
import lombok.extern.slf4j.Slf4j;
/**
 * model: 骑手放餐接口
 *
 * 场景一：骑手在小程序外卖订单里，直接扫柜子的格子码 输入：业务渠道id，柜子id,格子id 判断：若该格子id的格子状态为“空闲中”（否则输出：格子不可用）
 * 场景二：骑手在小程序外卖订单里，先查询柜子id，再按需选择格子类型 输入：业务渠道id，柜子id且格子类型
 * 判断：若该格子类型有格子状态为“空闲中”，则随机选中其一格（否则输出：无可用格子） 判断：若该格子id的格子状态为“空闲中”（否则输出格子不可用）
 * 输出：订单id，业务渠道id，柜子id，格子id，格子状态：交互中， 订单状态：订单生成， 订单生成时间， 下发开格指令

（1）格子状态为3空闲中、4超时，均允许骑手放餐，1 2状态则返回不可用
（2）若打开4超时的格口，则将上一订单的清货状态改为2已清货，且变为1交互中再变为2占用中

 *
 */

@Slf4j
@Service
public class RiderPutMealService extends HttpProvider {
	private final CounterRepository counterRepository;
	private final OrderRepository orderRepository; 
	private final OrderRecRepository orderRecRepository; 
	private static final String PATH = "http://boxcloud.uboxol.com/api/open_box?";
	private String v = "1.0";
    private String t = "" + System.currentTimeMillis() / 1000;
    private String app_id = "ubox_20150820";
    private String app_secret = "PzUqPozMduUc6RohPxVdPmtXNfzzL6";
	
	@Autowired 
	 public RiderPutMealService(final CounterRepository counterRepository,final OrderRepository orderRepository,final OrderRecRepository orderRecRepository) {
			 this.counterRepository = counterRepository;
			 this.orderRepository = orderRepository;
			 this.orderRecRepository = orderRecRepository;
	 }
	
	public EntryResult putMeal(RiderPutMeal req) { 
		EntryResult entryResult = new EntryResult();
		try {
			String bussinessId = req.getBussinessId();
			String cabinetId = req.getCabinetId();
			String gridId = req.getGridId();
			if(StringUtils.isEmpty(gridId)) {
				logger.info("格子id为空,场景二,自选格子");
				List<Counter> list1 = counterRepository.findByCabinetIdAndSpecsAndGridCurStatus(cabinetId, "中", 3);
				List<Counter> list2 = counterRepository.findByCabinetIdAndSpecsAndGridCurStatus(cabinetId, "中", 4);
				list1.addAll(list2);
				if(list1.size()==0) {
					entryResult.setCode("500");
					entryResult.setMsg("无可用格子");
					logger.info("无可用格子");
				}else {
					Counter counter = list1.get(0);
					entryResult = deal(bussinessId, counter);
				}
			}else {
				logger.info("格子id不为空,场景一,直接确定格子");
				Counter counter = counterRepository.findByCabinetIdAndGridId(cabinetId,gridId);
				if(counter==null) throw new RuntimeException("counter表中没有对应的格子");
				int gridCurStatus = counter.getGridCurStatus();//1交互中 2已占用 3空闲中 4超时
				if(gridCurStatus==3 || gridCurStatus==4) {
					entryResult = deal(bussinessId, counter);
				}else {
					entryResult.setCode("500");
					entryResult.setMsg("格子不可用");
					logger.info("格子不可用");
				}
			}
			
		}catch (Exception e) {
			logger.error("骑手放餐接口出错:{}", e.getMessage(), e); 
		}
		return entryResult;
	}
	
	public EntryResult deal(String bussinessId,Counter counter) {
		EntryResult entryResult = new EntryResult();
		OrderInformation orderInformation = null;
		try {
			//下发开格指令
			Map postmap = new HashMap();
			postmap.clear();
	        postmap.put("inner_code", counter.getCabinetId());
	        postmap.put("unitCode", counter.getGridId());
	        postmap.put("attach", "补货开盒");
	        postmap.put("unitType", "1");
	        HttpPost httpPost = new HttpPost(PATH + "app_id=" + app_id + "&v=" + v + "&t=" + t);
	        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
	        HttpResponse response = httpClient.execute(httpPost);
	        String value = consumeResponse(response.getEntity());
	        if(value.equals("1")) {
	        	logger.info("骑手放餐下发开格指令发送成功");
	        	
	        	if(counter.getGridCurStatus()==4) {//若打开4超时的格口，则将上一订单的清货状态改为2已清货
					Order order = orderRepository.findByOrderId(counter.getCurOrderId());
					order.setCleanStatus(2);
					order.setOrderStatus(4);
					order.setOrderClosedTime(new Timestamp(new Date().getTime()));
					orderRepository.saveAndFlush(order);
					logger.info("柜子："+counter.getCabinetId()+"格口"+counter.getGridId()+"当前状态为超时将上一订单的清货状态2订单状态4");
				}
				
	        	logger.info("新增格子和订单信息");
				orderInformation = addOrdeAndCounter(bussinessId, counter);
	        	
	        	entryResult.setCode("200");
				entryResult.setMsg("骑手放餐下发开格指令成功");
				entryResult.setOrderInformation(orderInformation);
	        }else {
	        	logger.info("柜子："+counter.getCabinetId()+"格口"+counter.getGridId()+"骑手放餐下发开格指令发送失败"+value+"(1-发送成功 0-发送失败 2-离线 -1-售货机不支持)");
	        	entryResult.setCode("500");
				entryResult.setMsg("骑手放餐下发开格指令失败");
	        }
		}catch (Exception e) {
			logger.error("骑手放餐接口出错:{}", e.getMessage(), e); 
		}
		return entryResult;
	}
	
	
	public OrderInformation addOrdeAndCounter(String bussinessId, Counter counter) {
		OrderInformation orderInformation = null;
		try {
			String orderId= HyfUtils.getFormatTime();
			logger.info("更新柜子信息");
			counter.setBussinessId(bussinessId);
			counter.setGridCurStatus(1);
			counter.setOrderCurStatus(1);
			counter.setCleanStatus(0);
			counter.setCurOrderId(orderId);
			counter.setCurTime(new Timestamp(new Date().getTime()));
			counterRepository.saveAndFlush(counter);
			logger.info("Counter表已经更新完毕");
			
			logger.info("新增订单信息");
			Order order = orderRepository.findByOrderId(orderId);
			if(order==null) {
				order = new Order();
			}
			order.setOrderId(orderId);
			order.setBussinessId(bussinessId);
			order.setCabinetId(counter.getCabinetId());
			order.setGridId(counter.getGridId());
			order.setSpecs(counter.getSpecs());
			order.setBranchCompany(counter.getBranchCompany());
			order.setPointId(counter.getPointId());
			order.setPointName(counter.getPointName());
			order.setGridStatus(1);
			order.setOrderStatus(1);
			order.setOrderTime(new Timestamp(new Date().getTime()));
			orderRepository.saveAndFlush(order);
			logger.info("Order表新增单子完毕");
			
			OrderRec orderRec = new OrderRec();
			orderRec.setOrderId(orderId);
			orderRec.setBussinessId(bussinessId);
			orderRec.setCabinetId(counter.getCabinetId());
			orderRec.setGridId(counter.getGridId());
			orderRec.setSpecs(counter.getSpecs());
			orderRec.setBranchCompany(counter.getBranchCompany());
			orderRec.setPointId(counter.getPointId());
			orderRec.setPointName(counter.getPointName());
			orderRec.setOrderReason("骑手送餐");
			orderRec.setCleanStatus(0);
			orderRec.setGridStatus(1);
			orderRec.setOrderStatus(1);
			orderRec.setOrderTime(new Timestamp(new Date().getTime()));
			orderRecRepository.saveAndFlush(orderRec);
			logger.info("orderRec记录表新增完毕");
			
			orderInformation = new OrderInformation();
			orderInformation.setOrderId(orderId);
			orderInformation.setBussinessId(bussinessId);
			orderInformation.setCabinetId(counter.getCabinetId());
			orderInformation.setGridId(counter.getGridId());
			orderInformation.setGridStatus(1);
			orderInformation.setOrderStatus(1);
			orderInformation.setOrderTime(order.getOrderTime());
			
		}catch (Exception e) {
			logger.error("入库接口出错:{}", e.getMessage(), e); 
		}
		return orderInformation;
		
	}
	
	
	
//	public void openGrid() {
//		try {
//			//下发开格指令
//			Map postmap = new HashMap();
//			postmap.clear();
//	        postmap.put("inner_code", "99900946");
//	        postmap.put("unit_code", "A04");
//	        postmap.put("unit_type", "1");
//	        HttpPost httpPost = new HttpPost("http://boxcloud.uboxol.com/api/open_box?" + "app_id=" + app_id + "&v=" + v + "&t=" + t);
//	        httpPost.setEntity(fillParam(app_secret, app_id, v, t, postmap));
//	        HttpResponse response = httpClient.execute(httpPost);
//	        String value = consumeResponse(response.getEntity());
//	        if(value.equals("1")) {
//	        	logger.info("下发开格指令发送成功");
//	        }else {
//	        	logger.info(value+"(1-发送成功 0-发送失败 2-离线 -1-售货机不支持)");
//	        }
//		}catch (Exception e) {
//			logger.error("接口出错:{}", e.getMessage(), e); 
//		}
//	}
	
	
	
}
