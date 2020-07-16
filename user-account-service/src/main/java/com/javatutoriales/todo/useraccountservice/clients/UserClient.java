package com.javatutoriales.todo.useraccountservice.clients;

import com.javatutoriales.todo.users.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface UserClient {

    String USERS_SERVICE_PATH = "/v1/users/";

    @GetMapping(USERS_SERVICE_PATH + "{username}")
    Optional<UserDto> findByUsername(@PathVariable String username);

    @PostMapping(USERS_SERVICE_PATH)
    UserDto saveUser(@RequestBody UserDto user);

    @PostMapping(USERS_SERVICE_PATH + "verify/{username}")
    Optional<UserDto> validateUserEmail(@PathVariable String username);
}