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

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable("id") String id) {

        log.info("/users/{}", id);

        return userService.findById(id).map(user -> ResponseEntity
                .ok()
                .eTag(user.getId())
                .location(URI.create(API_URL + "/" + user.getId()))
                .body(userAssembler.toModel(user))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
        log.info("Creating new user: {}", user);

        User newUser = userService.save(user);

        return ResponseEntity
                .created(URI.create(API_URL + "/" + newUser.getId()))
                .eTag(newUser.getId()) //TODO: replace id on etag for object version
                .body(userAssembler.toModel(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@RequestBody User user, @PathVariable String id) {
        Optional<User> existingUser = userService.findById(id);

        return (ResponseEntity<EntityModel<User>>) existingUser.map(u -> {
            u.setPassword(user.getPassword());

            if (userService.update(u)) {
                return ResponseEntity
                        .ok()
                        .location(URI.create(API_URL + "/" + u.getId()))
                        .body(userAssembler.toModel(u));
            } else {
                return ResponseEntity.notFound().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<User>> deleteUser(@PathVariable String id) {
        Optional<User> existingUser = userService.findById(id);

        return (ResponseEntity<EntityModel<User>>) existingUser.map(u -> {
            userService.delete(id);
            return ResponseEntity
                    .ok()
                    .location(URI.create(API_URL + "/" + u.getId()))
                    .body(userAssembler.toModel(u));

        }).orElse(ResponseEntity.notFound().build());
    }
}
