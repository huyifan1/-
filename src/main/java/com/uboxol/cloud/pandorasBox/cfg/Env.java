package com.uboxol.cloud.pandorasBox.cfg;

import org.springframework.core.env.AbstractEnvironment;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/10/25 15:09
 */
public enum Env {
    /**
     * 本地
     */
    LOCAL,
    /**
     * 开发
     */
    DEV,
    /**
     * 测试
     */
    REL,
    /**
     * 线上
     */
    PRO;

    public static Env of(int v) {
        return Env.values()[v];
    }

    public static Env getCurrent() {
        String property = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, Env.LOCAL.name());
        return Env.valueOf(property);
    }
}
