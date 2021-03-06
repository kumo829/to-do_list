package com.javatutoriales.todo.userservice.controller;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.model.User;
import com.javatutoriales.todo.userservice.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
@Api(description = "Users management operations.", tags = {"User service"}, produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private static final String API_URL = "/v1/users";

    private final UserService userService;
    private final UserResourceAssembler userAssembler;

    @GetMapping
    @ApiOperation(value = "Get all the users on the system", notes = "Result is not paginated, so potententially can get thousands of records",
            produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsers() {
        return ResponseEntity.ok(userAssembler.toCollectionModel(userService.findAll()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<EntityModel<UserDto>> getUser(@PathVariable("username") @NotEmpty String username) {

        log.info("/users/{}", username);

        return userService.findByUsername(username).map(user -> ResponseEntity
                .ok()
                .eTag(Integer.toString(user.getVersion()))
                .location(URI.create(API_URL + "/" + user.getUsername()))
                .body(userAssembler.toModel(user))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@RequestBody @Valid UserDto user) {
        log.info("Creating new user: {}", user);

        UserDto newUser = userService.save(user);

        return ResponseEntity
                .created(URI.create(API_URL + "/" + newUser.getUsername()))
                .eTag(Integer.toString(newUser.getVersion()))
                .body(userAssembler.toModel(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(@RequestBody @Valid UserDto user, @PathVariable String id) {

        return userService.update(id, user).map(u -> ResponseEntity
                .ok()
                .eTag(Integer.toString(u.getVersion()))
                .location(URI.create(API_URL + "/" + u.getId()))
                .body(userAssembler.toModel(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> deleteUser(@PathVariable String id) {

        return userService.delete(id).map(u -> ResponseEntity
                .ok()
                .body(userAssembler.toModel(u))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/verify/{username}")
    public Optional<UserDto> verifyEmail(@PathVariable String username) {
        return userService.verifyUser(username);
    }
}
