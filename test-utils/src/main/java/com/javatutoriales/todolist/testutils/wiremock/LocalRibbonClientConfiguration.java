package com.javatutoriales.todolist.testutils.wiremock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.springframework.cloud.netflix.ribbon.StaticServerList;

@TestConfiguration
public class LocalRibbonClientConfiguration {

    @Value("${wiremock.port}")
    int port;

    @Bean
    public ServerList<Server> ribbonServerList() {
        return new StaticServerList<>(new Server("localhost", port));
    }
}