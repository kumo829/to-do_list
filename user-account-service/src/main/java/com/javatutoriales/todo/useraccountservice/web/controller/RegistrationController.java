package com.javatutoriales.todo.useraccountservice.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/v1/register")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {
    @PostMapping
    Optional<User> registerUser(@RequestBody @Valid User user){
        return null;
    }

}
