package com.marco.ixigo.eventdispatcher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Standard SpringBoot configuration file used to configure the Swagger API
 * documentation
 * 
 * @author Marco
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Value("${com.marco.ixigo.eventdispatcher.version}")
    private String appVersion;
    
    @Bean
    public Docket swaggerConfiguration() {
        // @formatter:off
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.marco.ixigo.eventdispatcher.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
        // @formatter:on
    }

    private ApiInfo apiEndPointsInfo() {
        // @formatter:off
        return new ApiInfoBuilder()
                .title("Event Dispatcher")
                .description("This service will receive the CSGO Server events and dispatch them to all the listeners that has been registered")
                .contact(new Contact("Marco Solina", "marco.selfip.net", "marcosolina@gmail.com"))
                .version(appVersion)
                .build();
        // @formatter:on
    }

}
