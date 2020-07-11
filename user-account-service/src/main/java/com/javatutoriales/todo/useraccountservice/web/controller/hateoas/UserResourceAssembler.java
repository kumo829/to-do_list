package com.javatutoriales.todo.useraccountservice.web.controller.hateoas;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todolist.hateoas.SimpleIdentifiableRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends SimpleIdentifiableRepresentationModelAssembler<UserDto> {
    public UserResourceAssembler() {
        super(UserDto.class);
    }
}