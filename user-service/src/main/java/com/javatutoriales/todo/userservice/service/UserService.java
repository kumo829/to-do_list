package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.userservice.model.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> findAll();

    Optional<User> findById(String abc);

    User save(User user);

    boolean update(Object any);
}
