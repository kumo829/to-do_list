package com.javatutoriales.todo.useraccountservice.clients;

import com.javatutoriales.todo.users.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {

    String USERS_SERVICE_PATH = "/v1/users/";

    @GetMapping(USERS_SERVICE_PATH + "{username}")
    Optional<User> findByUsername(@PathVariable String username);

    @PostMapping(USERS_SERVICE_PATH)
    Optional<User> saveUser(@RequestBody User user);
}