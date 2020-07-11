package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.users.dto.UserDto;

public interface UserService {
    UserDto registerUser(UserDto userDto);
}
