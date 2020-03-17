package com.uboxol.cloud.pandorasBox.cfg;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.uboxol.cloud.pandorasBox.service.remote.CdsApiTransportServiceConfig;
import com.uboxol.cloud.pandorasBox.service.remote.NodeTransportServiceConfig;
import com.uboxol.cloud.pandorasBox.service.remote.VmRemoteServiceConfig;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:27
 */
@Configuration
@Import({
    CdsApiTransportServiceConfig.class,
    NodeTransportServiceConfig.class,
    VmRemoteServiceConfig.class
})
public class RemoteThriftAutoConfiguration {
}
