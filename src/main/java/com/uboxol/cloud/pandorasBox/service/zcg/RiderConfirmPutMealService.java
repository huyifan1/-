package com.uboxol.cloud.pandorasBox.service.zcg;

import java.sql.Timestamp;
import java.util.Date;
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
 * 骑手确认放餐接口
 * 
 * 输入：业务渠道id，柜子id,格子id，确认放餐
	输出：订单id，业务方id，柜子id，格子id，格子状态：已占用，
           订单状态：订单生成，
           订单生成时间，
           订单状态：订单确认，
           订单确认时间
*/

@Slf4j
@Service
public class RiderConfirmPutMealService {
	private final CounterRepository counterRepository;
	private final OrderRepository orderRepository; 
	private final OrderRecRepository orderRecRepository; 
	
	@Autowired 
	 public RiderConfirmPutMealService(final CounterRepository counterRepository,final OrderRepository orderRepository,final OrderRecRepository orderRecRepository) {
			 this.counterRepository = counterRepository;
			 this.orderRepository = orderRepository;
			 this.orderRecRepository = orderRecRepository;
	 }

	public EntryResult confirmPutMeal(Grid req) { 
		EntryResult entryResult = new EntryResult();
		OrderInformation orderInformation = null;
		try {
			Counter counter = counterRepository.findByCabinetIdAndGridId(req.getCabinetId(),req.getGridId());
			if(counter==null) throw new RuntimeException("counter表中没有对应的格子");
			counter.setGridCurStatus(2);
			counter.setOrderCurStatus(2);
			counter.setCurTime(new Timestamp(new Date().getTime()));
			counterRepository.saveAndFlush(counter);
			logger.info("更新格子状态为已占用");
			
			Order order =orderRepository.findByOrderId(counter.getCurOrderId());
			if(order==null) throw new RuntimeException("order表中没有对应的订单");
			order.setGridStatus(2);
			order.setOrderStatus(2);
			order.setOrderConfirmTime(new Timestamp(new Date().getTime()));
			orderRepository.saveAndFlush(order);
			logger.info("更新订单状态为订单确认");
			
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
			orderRec.setOrderReason("骑手确认送餐");
			orderRec.setCleanStatus(0);
			orderRec.setGridStatus(2);
			orderRec.setOrderStatus(2);
			orderRec.setOrderConfirmTime(new Timestamp(new Date().getTime()));
			orderRecRepository.saveAndFlush(orderRec);
			logger.info("orderRec记录表新增完毕");
			
			orderInformation = new OrderInformation();
			orderInformation.setOrderId(order.getOrderId());
			orderInformation.setBussinessId(order.getBussinessId());
			orderInformation.setCabinetId(order.getCabinetId());
			orderInformation.setGridId(order.getGridId());
			orderInformation.setGridStatus(2);
			orderInformation.setOrderStatus(2);
			orderInformation.setOrderTime(order.getOrderTime());
			orderInformation.setOrderConfirmTime(order.getOrderConfirmTime());
			
			entryResult.setCode("200");
			entryResult.setMsg("骑手确认放餐成功");
			entryResult.setOrderInformation(orderInformation);
			
		}catch (Exception e) {
			logger.error("骑手确认放餐接口出错:{}", e.getMessage(), e); 
		}
		return entryResult;
	}
}
