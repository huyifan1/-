package com.uboxol.cloud.pandorasBox.service.zcg;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.api.res.ClearResult;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Order;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.OrderRec;
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
public class CleanGoodsService {
	private final CounterRepository counterRepository;
	private final OrderRepository orderRepository; 
	private final OrderRecRepository orderRecRepository; 
	
	@Autowired 
	 public CleanGoodsService(final CounterRepository counterRepository,final OrderRepository orderRepository,final OrderRecRepository orderRecRepository) {
	 	 this.counterRepository = counterRepository;
		 this.orderRepository = orderRepository;
		 this.orderRecRepository = orderRecRepository;
 }
	
	public ClearResult cleanGoods(ClearGrids req) { 
		ClearResult clearResult = new ClearResult();
		List<OrderInformation> list = new ArrayList<OrderInformation>();
		try {
			String cabinetId = req.getCabinetId();
			List<String> gridIds = req.getGridIds();
			for(String gridId : gridIds) {
				OrderInformation o = clear(cabinetId,gridId);
				list.add(o);
			}
			clearResult.setCode("200");
			clearResult.setMsg("清货通知成功");
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
			counter.setCurTime(new Timestamp(new Date().getTime()));
			counter.setCleanStatus(1);
			counter.setGridCurStatus(4);//收到清货通知，格子状态改为4超时
			counterRepository.saveAndFlush(counter);
			logger.info("更新格子状态为超时");
			
			Order order =orderRepository.findByOrderId(counter.getCurOrderId());
			if(order==null) throw new RuntimeException("order表中没有对应的订单");
			order.setCleanStatus(1);
			order.setGridStatus(4);
			//order.setOrderClosedTime(new Timestamp(new Date().getTime()));
			orderRepository.saveAndFlush(order);
			logger.info("更新订单清货状态为待清货");
			
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
			orderRec.setOrderReason("清货通知");
			orderRec.setCleanStatus(1);
			
			orderRecRepository.saveAndFlush(orderRec);
			logger.info("orderRec记录表新增完毕");
			
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
			
		}catch (Exception e) {
			logger.error("清货通知接口出错:{}", e.getMessage(), e); 
		}
		return orderInformation;
	}
	
}