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
//@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserResourceAssembler userAssembler;

    @GetMapping("/users")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getUsers() {
        return ResponseEntity.ok(userAssembler.toCollectionModel(userService.findAll()));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> getUser(@PathVariable("id") String id) {

        log.info("/users/{}", id);

        return userService.findById(id).map(user -> ResponseEntity
                .ok()
                .eTag(user.getId())
                .location(URI.create("/users/" + user.getId()))
                .body(userAssembler.toModel(user))).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<User>> createUser(@RequestBody User user) {
        log.info("Creating new user: {}", user);

        User newUser = userService.save(user);

        return ResponseEntity
                .created(URI.create("/users/" + newUser.getId()))
                .eTag(newUser.getId()) //TODO: replace id on etag for object version
                .body(userAssembler.toModel(newUser));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<EntityModel<User>> updateUser(@RequestBody User user, @PathVariable String id) {
        Optional<User> existingUser = userService.findById(id);

        return (ResponseEntity<EntityModel<User>>) existingUser.map(u -> {
            u.setUsername(user.getUsername());

            if (userService.update(u)) {
                return ResponseEntity
                        .ok()
                        .location(URI.create("/users/" + u.getId()))
                        .body(userAssembler.toModel(u));
            } else {
                return ResponseEntity.notFound().build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }
}
