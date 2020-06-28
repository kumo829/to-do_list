package com.javatutoriales.todo.userservice.controller.hateoas;

import com.javatutoriales.todo.users.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler extends SimpleIdentifiableRepresentationModelAssembler<User> {
    public UserResourceAssembler() {
        super(User.class);
    }
}
