package com.javatutoriales.todo.useraccountservice.web.controller;

import com.javatutoriales.todo.useraccountservice.UserService;
import com.javatutoriales.todo.useraccountservice.web.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Validated
@RestController
@RequestMapping("/v1/register")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {

    private static final String API_URL = "/v1/register";


    private final UserService userService;
    private final UserResourceAssembler userAssembler;

    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> registerUser(@RequestBody @Validated UserDto user){

        UserDto newUser = userService.registerUser(user);

        return ResponseEntity.created(URI.create(API_URL + "/" + newUser.getUsername()))
                .eTag(Integer.toString(newUser.getVersion()))
                    .body(userAssembler.toModel(newUser));
    }

}
