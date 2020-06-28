package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.users.model.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> findAll();

    Optional<User> findByUsername(String username);

    Optional<User> findById(String id);

    User save(User user);

    Optional<User> update(String userId, User user);

    Optional<User> delete(String id);
}
