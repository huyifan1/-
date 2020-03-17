package com.uboxol.cloud.pandorasBox.service.remote;

import org.springframework.context.annotation.Bean;

import com.uboxol.cloud.aop.ThriftClientProxy;
import com.uboxol.cloud.pandorasBox.cfg.Config;
import cn.ubox.cloud.node.VmRemoteService;

public class VmRemoteServiceConfig {
	private final static String SERVICE_NAME = Config.VM_SERVICE_NAME;

    @Bean
    public VmRemoteService.Iface vmRemoteService() {
        return new ThriftClientProxy<VmRemoteService.Client, VmRemoteService.Iface>(SERVICE_NAME)
            .bind(VmRemoteService.Client.class);
    }
}
