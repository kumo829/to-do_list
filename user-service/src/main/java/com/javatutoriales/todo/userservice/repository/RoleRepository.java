package com.javatutoriales.todo.userservice.repository;

import com.javatutoriales.todo.userservice.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
