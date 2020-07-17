package com.javatutoriales.todo.useraccountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
@Import({BeanValidatorPluginsConfiguration.class})
public class ApplicationSwaggerConfig {
    @Bean
    public Docket usersApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("user-account-api-v1")
                .select()
                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any())
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                .apiInfo(getInfoApi());
    }

    private ApiInfo getInfoApi() {
        return new ApiInfoBuilder()
                .title("User Account API")
                .version("1.0")
                .description("API for managing Users' account")
                .contact(new Contact("Alex Montoya", "http://javatutoriales.com", "programadorjavablog@gmail.com"))
                .license("Apache License Version 2.0")
                .build();
    }
}
