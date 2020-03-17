package com.uboxol.cloud.pandorasBox;

import com.uboxol.cloud.pandorasBox.cfg.Env;
import com.uboxol.cloud.pandorasBox.middleware.EnvironmentPreparedEventListener;
import com.uboxol.cloud.pandorasBox.middleware.MermaidFailedEventListener;
import com.uboxol.cloud.pandorasBox.middleware.MermaidStartedEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liyunde
 * @since 2019/10/18 15:09
 */
@Slf4j
@EnableScheduling
//@EnableDiscoveryClient
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class
})
public class PandorasBoxApplication {

    /**
     * 系统运行环境 变量
     */
    private static final String SERVICE_ENV = "service.env";

    public static void main(String[] args) {

        final String env = System.getProperty(SERVICE_ENV, Env.LOCAL.name());

        Env.valueOf(env);

        logger.debug("启动环境:{}", env);

        System.setProperty(SERVICE_ENV, env);

        if (Env.valueOf(env) != Env.PRO) {
            //cds 服务都在 REL 上面
            logger.debug("只要不是线上,开发时注册到REL上");
            System.setProperty(SERVICE_ENV, Env.REL.name());
        }

        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, env);

        final String profiles = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Env.PRO.name());

        logger.debug("profile: {}", profiles);

//        System.setProperty("druid.registerToSysProperty","true");
        SpringApplication application = new SpringApplication(PandorasBoxApplication.class);

        application.addListeners(
            new EnvironmentPreparedEventListener(),
            new MermaidStartedEventListener(),
            new MermaidFailedEventListener()
        );

        application.run(args);
    }
}
