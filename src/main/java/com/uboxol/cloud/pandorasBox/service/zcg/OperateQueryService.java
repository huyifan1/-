package com.uboxol.cloud.pandorasBox.service.zcg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uboxol.cloud.pandorasBox.api.req.OperateQuery;
import com.uboxol.cloud.pandorasBox.api.res.OperateQueryResult;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class OperateQueryService extends HttpProvider {
	private final CounterRepository counterRepository;
    
	
	@Autowired 
	 public OperateQueryService(final CounterRepository counterRepository) {
	 	 this.counterRepository = counterRepository;
	 }
	
	public OperateQueryResult operate(OperateQuery req) { 
		OperateQueryResult clearResult = new OperateQueryResult();
		try {
			
			List<Counter> counters = counterRepository.findByCleanStatus(req.getCleanStatus());
			if(counters.size()==0) {
				clearResult.setCode("500");
				clearResult.setMsg("没有查到待清货的格子");
			}else {
				clearResult.setCode("200");
				clearResult.setMsg("查询待清货格子成功");
				clearResult.setList(counters);
			}
			
		}catch (Exception e) {
			logger.error("查询可清货的格子接口出错:{}", e.getMessage(), e); 
		}
		return clearResult;
	}
	
	
}