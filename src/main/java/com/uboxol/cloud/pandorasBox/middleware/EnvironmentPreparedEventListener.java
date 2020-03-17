package com.uboxol.cloud.pandorasBox.middleware;

import com.uboxol.util.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:20
 */
@Slf4j
public class EnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(@NonNull ApplicationEnvironmentPreparedEvent event) {

        String zkList = event.getEnvironment().getProperty("cfg.zk-list");

        logger.info("event:{},zk:{}", event, zkList);

        ZkUtils.setServerList(zkList);
    }
}
