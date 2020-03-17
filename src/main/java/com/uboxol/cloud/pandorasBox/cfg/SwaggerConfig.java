package com.uboxol.cloud.pandorasBox.cfg;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableSwagger2
//@ConditionalOnProperty(prefix = "msg-config", name = "swagger-open", havingValue = "true")
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //.apis(RequestHandlerSelectors.withClassAnnotation(ApisController.class))                      //这里采用包含注解的方式来确定要显示的接口
                .apis(RequestHandlerSelectors.basePackage("com.uboxol.cloud.pandorasBox.api"))    //这里采用包扫描的方式来确定要显示的接口
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("MoerService Gateway Doc")                       //标题
                .description("暂存柜中台服务Api文档")                  //描述
               // .termsOfServiceUrl("http://www.moerlong.com")           //条款地址（不可见）
                .contact("huyifan")                                    //作者信息
                .version("1.0")                                         //版本号
                .build();
    }

}
