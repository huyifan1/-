package com.uboxol.cloud.pandorasBox.service.zcg;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.liaochong.myexcel.utils.StringUtil;
import com.uboxol.cloud.pandorasBox.api.req.ClearGrids;
import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.db.repository.zcg.CounterRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * 查询某个柜子下所以空闲格子(只需输入柜子ID)
*/

@Slf4j
@Service
public class QueryFreeGridsService {
	private final CounterRepository counterRepository;
	
	@Autowired 
	 public QueryFreeGridsService(final CounterRepository counterRepository) {
	 	 this.counterRepository = counterRepository;
	 }
	
	public List<Counter> query(ClearGrids req) {
		List<Counter> list1 = new ArrayList<Counter>();
		try {
			if(StringUtil.isNotBlank(req.getCabinetId())) {
				list1 = counterRepository.findByCabinetIdAndGridCurStatus(req.getCabinetId(), 3);
				List<Counter> list2 = counterRepository.findByCabinetIdAndGridCurStatus(req.getCabinetId(), 4);
				list1.addAll(list2);
			}
		}catch (Exception e) {
			logger.error("查询可清货的格子接口出错:{}", e.getMessage(), e); 
		}
		return list1;
	}
	
	
	
	
	
	
	
	
	
	
	
}