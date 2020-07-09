package com.javatutoriales.todo.userservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {
    @Value("${service.version:1.0.0}")
    private String serviceVersion;

    @GetMapping
    public String ping(){
        return serviceVersion;
    }
}
