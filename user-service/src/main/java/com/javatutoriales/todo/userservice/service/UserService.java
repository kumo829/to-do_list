package com.javatutoriales.todo.userservice.service;


import com.javatutoriales.todo.users.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Iterable<UserDto> findAll();

    Optional<UserDto> findByUsername(String username);

    Optional<UserDto> findById(String id);

    UserDto save(UserDto user);

    Optional<UserDto> update(String userId, UserDto user);

    Optional<UserDto> delete(String id);
}
