package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.useraccountservice.clients.UserClient;
import com.javatutoriales.todo.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;

    @Override
    public UserDto registerUser(UserDto userDto) {
        return userClient.saveUser(userDto);
    }
}
