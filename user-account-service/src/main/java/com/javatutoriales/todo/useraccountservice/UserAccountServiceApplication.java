package com.javatutoriales.todo.useraccountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableFeignClients
@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
public class UserAccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAccountServiceApplication.class, args);
    }

}
