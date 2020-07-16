package com.javatutoriales.todo.useraccountservice.events;

import com.javatutoriales.todo.users.dto.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class UserRegistrationEvent extends ApplicationEvent {

    @Getter
    private final UserDto user;

    public UserRegistrationEvent(UserDto user) {
        super(user);
        this.user = user;
    }
}
