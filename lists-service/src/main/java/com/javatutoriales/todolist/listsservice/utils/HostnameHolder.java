package com.javatutoriales.todolist.listsservice.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Slf4j
public class HostnameHolder {

    @Value("${server.address:}")
    private String serverAddress;

    private String hostname;

    @PostConstruct
    public void postConstruct() {
        if (!"".equals(serverAddress)) {
            hostname = serverAddress;
        } else {
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                hostname = "<unknown>";
            }
        }

        log.info("Hostname: {}", hostname);
    }

    public String getHostname() {
        return hostname;
    }
}
