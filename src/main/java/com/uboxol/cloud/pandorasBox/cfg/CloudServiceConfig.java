package com.uboxol.cloud.pandorasBox.cfg;

import com.uboxol.cloud.tengu.common.server.configuration.AlipayServiceConfiguration;
import com.uboxol.cloud.tengu.common.server.swagger.SwaggerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * model: cds-os
 *
 * @author liyunde
 * @since 2019/9/11 10:57
 */
@Configuration
//@ConditionalOnProperty(prefix = "msg-config", name = "swagger-open", havingValue = "false")
public class CloudServiceConfig extends AlipayServiceConfiguration {

    /**
     * 开启文档，微服务自动注册文档地址
     */
    @Configuration
    @EnableSwagger2
    public static class Swagger2 extends SwaggerSupport {
        @Override
        protected String getDocTitle() {
            return "友宝暂存柜Api服务";
        }

        @Override
        protected ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                .title(getDocTitle())
                .description("暂存柜中台服务Api文档")
               // .termsOfServiceUrl("友宝服务条款")
                .version("2020.1.0")
                .contact(contact())
                .build();
        }

        /**
         * 当前维护者联系信息
         *
         * @return Contact
         */
        protected Contact contact() {
            return new Contact("huyifan", null, "huyifan@ubox.cn");
        }
    }

    @Bean
    public CorsFilter webCorsFilter() {
        //swagger ,WebMvcConfigurer 和 WebMvcConfigurationSupport 都不起作用,这里只能用过滤器处理
        //参考: https://docs.spring.io/spring/docs/5.0.6.RELEASE/spring-framework-reference/web.html#mvc-cors-global

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.addAllowedOrigin("http://swagger.ubox.liyunde.com");
        config.addAllowedOrigin("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/v2/**", config);

        return new CorsFilter(source);
    }
}
