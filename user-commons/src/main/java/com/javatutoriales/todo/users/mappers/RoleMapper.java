package com.javatutoriales.todo.users.mappers;

import com.javatutoriales.todo.users.dto.RoleDto;
import com.javatutoriales.todo.users.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);

    Role roleDtoToRole(RoleDto roleDto);
}
