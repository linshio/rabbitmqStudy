package cn.linshio.rabbitmq.springbootrabbitmq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2 //开启丝袜哥
public class ApplicationConfiguration {

    @Bean
    public Docket WebApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("wenApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("rabbitmq接口文档")
                .description("本文档描述了rabbitmq微服务接口的定义")
                .version("1.0")
                .contact(new Contact("linshio","http://linshio.cn","3361445513@qq.com"))
                .build();
    }
}
