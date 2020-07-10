package com.javatutoriales.todo.useraccountservice;

import com.javatutoriales.todo.users.dto.UserDto;

public interface UserService {
    UserDto registerUser(UserDto userDto);
}
