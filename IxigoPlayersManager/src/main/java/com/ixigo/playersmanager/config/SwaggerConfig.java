package com.ixigo.playersmanager.config;
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

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${com.ixigo.playersmanager.version}")
    private String appVersion;
    
    @Bean
    public Docket swaggerConfiguration() {
        // @formatter:off
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.ixigo.playersmanager.controllers"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiEndPointsInfo());
        // @formatter:on
    }

    private ApiInfo apiEndPointsInfo() {
        // @formatter:off
        return new ApiInfoBuilder()
                .title("Ixigo Players manager")
                .description("Service which will manage the CSGO GOTV DEM files")
                .contact(new Contact("Marco Solina", "https://marco.selfip.net", "marcosolina@gmail.com"))
                .version(appVersion)
                .build();
        // @formatter:on
    }
}
