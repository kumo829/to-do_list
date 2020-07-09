package com.javatutoriales.todo.userservice.controller.hateoas;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends SimpleIdentifiableRepresentationModelAssembler<UserDto> {
    public UserResourceAssembler() {
        super(UserDto.class);
    }
}
