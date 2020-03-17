package com.uboxol.cloud.pandorasBox;

import com.uboxol.cloud.pandorasBox.cfg.Env;
import com.uboxol.util.ZkUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/31 13:21
 */
@ActiveProfiles("LOCAL")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PandorasBoxApplication.class)
public abstract class BaseSpringBootTest {

    protected static final String ENV;

    static {
        System.setProperty("service.env", "REL");
        System.setProperty("sys.env.work-id", "1");

        ENV = Env.getCurrent().name();

        ZkUtils.setServerList("zk01.test.ubox:2181");
    }

    @Autowired
    protected ApplicationContext context;
}
