package com.javatutoriales.todo.useraccountservice.events;

import com.javatutoriales.todo.users.dto.UserDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class EmailValidationEvent extends ApplicationEvent {

    @Getter
    private UserDto user;

    public EmailValidationEvent(UserDto user) {
        super(user);
        this.user = user;
    }
}
