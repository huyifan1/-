package com.uboxol.cloud.pandorasBox.service.zcg;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.uboxol.cloud.pandorasBox.api.req.Grid;
import com.uboxol.cloud.pandorasBox.api.res.OrderInformation;

import lombok.extern.slf4j.Slf4j;

	@Slf4j
	@Service
	public class CustomerOpenGridService {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd");

		//客服开格-需求讨论中
		public OrderInformation openGrid(Grid req) { 
			OrderInformation orderInformation = null;
			try {
				
				
				
				
				
			}catch (Exception e) {
				logger.error("客服开格查询出错:{}", e.getMessage(), e); 
			}
			return orderInformation;
		}
	}
