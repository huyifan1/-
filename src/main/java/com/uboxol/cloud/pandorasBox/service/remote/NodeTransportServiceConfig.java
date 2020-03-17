package com.uboxol.cloud.pandorasBox.service.remote;

import cn.ubox.cloud.node.NodeRemoteService;
import com.uboxol.cloud.aop.ThriftClientProxy;
import com.uboxol.cloud.pandorasBox.cfg.Config;
import org.springframework.context.annotation.Bean;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/28 18:11
 */
public class NodeTransportServiceConfig {

    private final static String SERVICE_NAME = Config.NODE_SERVICE_NAME;

    @Bean
    public NodeRemoteService.Iface nodeTransportService() {
        return new ThriftClientProxy<NodeRemoteService.Client, NodeRemoteService.Iface>(SERVICE_NAME)
            .bind(NodeRemoteService.Client.class);
    }
}
