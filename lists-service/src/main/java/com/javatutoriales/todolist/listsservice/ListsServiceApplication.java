package com.javatutoriales.todolist.listsservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javatutoriales.todolist.listsservice.utils.HostnameHolder;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
public class ListsServiceApplication {

    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(ListsServiceApplication.class, args);
    }

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer(HostnameHolder hostnameHolder) {
        return registry -> registry.config().commonTags("hostname", hostnameHolder.getHostname());
    }
}
