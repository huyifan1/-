package com.uboxol.cloud.pandorasBox.middleware;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/21 13:06
 */
@Slf4j
public class MermaidFailedEventListener implements ApplicationListener<ApplicationFailedEvent> {
    @Override
    public void onApplicationEvent(@NonNull final ApplicationFailedEvent event) {

        logger.info("处理 {} 事件,服务启动失败...", event.getClass().getSimpleName());

        //发送告警邮件等
    }
}
