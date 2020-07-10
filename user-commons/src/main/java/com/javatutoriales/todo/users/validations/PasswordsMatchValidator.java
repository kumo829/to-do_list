package com.javatutoriales.todo.users.validations;


import com.javatutoriales.todo.users.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserDto> {
    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getPasswordConfirmation());
    }
}
