package com.javatutoriales.todo.users.mappers;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.model.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserDto userToUserDto(User user);

    User usetDtoToUser(UserDto userDto);
}
