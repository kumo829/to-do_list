package com.javatutoriales.todo.oauthservice.clients;

import com.javatutoriales.todo.users.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/users/{username}")
    Optional<User> findByUsername(@PathVariable String username);
}
