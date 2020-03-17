package com.uboxol.cloud.pandorasBox.api.req;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springfox.documentation.annotations.ApiIgnore;

/**
 * model: mermaid
 *
 * @author liyunde
 * @since 2019/11/4 14:55
 */
@Getter
@Setter
@ToString
@ApiIgnore
public final class UserInject {
    /**
     * 注入用户名
     * 外部请求的用户名字
     */
    String user;

    public UserInject(final String user) {
        this.user = user;
    }
}
