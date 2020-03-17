package com.uboxol.cloud.pandorasBox.service.remote;

import com.uboxol.cloud.aop.ThriftClientProxy;
import com.uboxol.cloud.pandorasBox.cfg.Config;
import com.uboxol.gen.api.Cds2APIService;
import org.springframework.context.annotation.Bean;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:26
 */
public class CdsApiTransportServiceConfig {
    private final static String SERVICE_NAME = Config.CDS_API_SERVICE_NAME;

    @Bean
    public Cds2APIService.Iface apiTransportService() {
        return new ThriftClientProxy<Cds2APIService.Client, Cds2APIService.Iface>(SERVICE_NAME)
            .bind(Cds2APIService.Client.class);
    }
}
