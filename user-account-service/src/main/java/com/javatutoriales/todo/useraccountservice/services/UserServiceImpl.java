package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.useraccountservice.clients.UserClient;
import com.javatutoriales.todo.useraccountservice.events.UserRegistrationEvent;
import com.javatutoriales.todo.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClient userClient;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public UserDto registerUser(UserDto userDto) {
        userDto.setEnabled(false);

        UserDto newUser = userClient.saveUser(userDto);
        eventPublisher.publishEvent(new UserRegistrationEvent(newUser));

        return newUser;
    }
}
