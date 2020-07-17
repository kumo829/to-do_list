package com.javatutoriales.todo.useraccountservice.web.controller;

import com.javatutoriales.todo.useraccountservice.services.UserService;
import com.javatutoriales.todo.useraccountservice.web.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.users.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
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
@Api(description = "Users management operations.", tags = {"User service"}, produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

    private static final String API_URL = "/v1/register";

    private final UserService userService;
    private final UserResourceAssembler userAssembler;

    @PostMapping
    @ApiOperation(value = "Create a new user account on the system", notes = "Username and email must be unique in the system",
            produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created uer"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<EntityModel<UserDto>> registerUser(@RequestBody @Validated UserDto user) {

        UserDto newUser = userService.registerUser(user);

        return ResponseEntity.created(URI.create(API_URL + "/" + newUser.getUsername()))
                .eTag(Integer.toString(newUser.getVersion()))
                .body(userAssembler.toModel(newUser));
    }

}
