package com.uboxol.cloud.pandorasBox.service.zcg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uboxol.cloud.pandorasBox.api.req.Grid;
import com.uboxol.cloud.pandorasBox.api.res.EntryResult;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;

import lombok.extern.slf4j.Slf4j;
/*
 * 二次开格接口
 * 
 输入：业务渠道id，柜子id,格子id，再次开格
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
public class RepeatOpenGridService {
	private final CounterRepository counterRepository;
	private final UserTakeMealService userTakeMealService;
	
	@Autowired 
	 public RepeatOpenGridService(final CounterRepository counterRepository,final UserTakeMealService userTakeMealService) {
			 this.counterRepository = counterRepository;
			 this.userTakeMealService = userTakeMealService;
	 }

	public EntryResult repeatTakeMeal(Grid req) { 
		EntryResult entryResult = new EntryResult();
		try {
			Counter counter = counterRepository.findByCabinetIdAndGridId(req.getCabinetId(),req.getGridId());
			if(counter==null) throw new RuntimeException("counter表中没有对应的格子");
			int status = counter.getGridCurStatus();
			if(status!=3) {
				if(counter.getBussinessId().equals(req.getBussinessId())) {
					entryResult = userTakeMealService.takeMeal(req);
					entryResult.setMsg("二次开格成功");
				}else {
					entryResult.setCode("500");
					entryResult.setMsg("渠道不一致，格子目前渠道"+status+"此次渠道"+req.getBussinessId());
				}
			}else {
				entryResult = userTakeMealService.takeMeal(req);
				entryResult.setMsg("二次开格成功");
			}
			
		}catch (Exception e) {
			logger.error("二次开格接口出错:{}", e.getMessage(), e); 
		}
		return entryResult;
	}
}
