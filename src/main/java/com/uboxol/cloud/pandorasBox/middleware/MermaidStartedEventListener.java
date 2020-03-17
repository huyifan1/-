package com.uboxol.cloud.pandorasBox.middleware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/21 13:03
 */
@Slf4j
public class MermaidStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {
    /**
     * 当前服务注册名字
     */
    private static final String SERVICE_NAME = "pandorasBox";

    @Override
    public void onApplicationEvent(@NonNull final ApplicationStartedEvent event) {

        logger.debug("处理 ApplicationStartedEvent 事件,服务已启动.");


    }
}
