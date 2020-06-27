package com.javatutoriales.todo.userservice.controller;

import com.javatutoriales.todo.userservice.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.userservice.model.User;
import com.javatutoriales.todo.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users") //TODO: Update route in zuul server
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private static final String API_URL = "/api/v1/users";

    private final UserService userService;
    private final UserResourceAssembler userAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers() {
        return ResponseEntity.ok(userAssembler.toCollectionModel(userService.findAll()));
    }

    @GetMapping("/{username}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable("username") String username) {

        log.info("/users/{}", username);

        return userService.findByUsername(username).map(user -> ResponseEntity
                .ok()
                .eTag(Integer.toString(user.getVersion()))
                .location(URI.create(API_URL + "/" + user.getId()))
                .body(userAssembler.toModel(user))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
        log.info("Creating new user: {}", user);

        User newUser = userService.save(user);

        return ResponseEntity
                .created(URI.create(API_URL + "/" + newUser.getId()))
                .eTag(Integer.toString(newUser.getVersion()))
                .body(userAssembler.toModel(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@RequestBody User user, @PathVariable String id) {
        Optional<User> existingUser = userService.findById(id);

        return existingUser.map(u -> {
            u.setPassword(user.getPassword());

            User updatedUser = userService.update(u);

            return ResponseEntity
                    .ok()
                    .eTag(Integer.toString(updatedUser.getVersion()))
                    .location(URI.create(API_URL + "/" + u.getId()))
                    .body(userAssembler.toModel(updatedUser));

        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<User>> deleteUser(@PathVariable String id) {
        Optional<User> existingUser = userService.findById(id);

        return existingUser.map(u -> {
            userService.delete(id);
            return ResponseEntity
                    .ok()
                    .body(userAssembler.toModel(u));

        }).orElse(ResponseEntity.notFound().build());
    }
}
