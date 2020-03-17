package com.uboxol.cloud.pandorasBox.task;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.uboxol.cloud.pandorasBox.db.entity.zcg.Counter;
import com.uboxol.cloud.pandorasBox.service.zcg.CabinetInformationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScheduleServiceTask {

    private final CabinetInformationService cabinetInformationService;
    @Autowired
    public ScheduleServiceTask(final CabinetInformationService cabinetInformationService) {
        this.cabinetInformationService = cabinetInformationService; 
    }

    @Scheduled(cron ="* 10 16 * * ?" )
    public void scheduled1() {
    	 
        logger.info("添加新格子信息入库开始");
        
        List<Counter> counterList = cabinetInformationService.update();
        
        logger.info("添加新格子信息入库结束"+counterList.toString());
    }

    

}
