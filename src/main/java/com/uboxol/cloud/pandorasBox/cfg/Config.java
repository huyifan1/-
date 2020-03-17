package com.uboxol.cloud.pandorasBox.cfg;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:12
 */
@Getter
@Setter
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "cfg")
public class Config implements EnvironmentAware, Ordered {

    public static final String SERVICE_NAME = "pandorasBox";

    public static final String CDS_API_SERVICE_NAME = "Cds2APIService";

    public static final String NODE_SERVICE_NAME = "NodeRemoteService";
    
    public static final String VM_SERVICE_NAME = "VmRemoteService";
    
    private Environment environment;

    private String zkList;

    private String version;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
