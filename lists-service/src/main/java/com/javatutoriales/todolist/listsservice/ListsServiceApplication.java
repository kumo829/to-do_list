package com.javatutoriales.todolist.listsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ListsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListsServiceApplication.class, args);
    }

}
