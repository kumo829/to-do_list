package com.javatutoriales.todo.userservice.repository;

import com.javatutoriales.todo.userservice.model.Role;
import com.javatutoriales.todo.userservice.model.User;
import com.javatutoriales.todo.userservice.repository.mongo.MongoDataFile;
import com.javatutoriales.todo.userservice.repository.mongo.MongoSpringExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("testing")
@ExtendWith(MongoSpringExtension.class)
public class UserRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @Test
    @DisplayName("Save User - SUCCESS")
    @MongoDataFile(value = "roles.json", classType = Role.class, collectionName = "roles")
    public void whenSaveNewUser_thenNewUserOnDatabaseWithUserRole(){
        LocalDateTime now = LocalDateTime.now();

        Optional<Role> userRoleOptional = roleRepository.findByName("User");
        Role userRole = userRoleOptional.get();

        User newUser = User.builder().username("newUser").email("user@mail.com").password("hasdifsdhf").version(1).build();

        newUser.addRole(userRole);

        User savedUser = userRepository.save(newUser);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull().isNotEmpty().isNotBlank();
        assertThat(savedUser.getCreatedDate()).isNotNull().isAfter(now);

        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(newUser.getPassword());
    }

    @Test
    @DisplayName("Get all Users - SUCCESS")
    @MongoDataFile(value = "users.json", classType = User.class, collectionName = "users")
    void whenGetAllUsers_thenUsersListHasFourUsers() {
        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(4);
        assertThat(users.get(0).getId()).isEqualTo("e140057d-a6b6-437c-89be-8ab7b7a25572");
        assertThat(users.get(0).getUsername()).isEqualTo("newUser");
        assertThat(users.get(0).getEmail()).isEqualTo("user@mail.com");
        assertThat(users.get(0).getRoles()).isNotNull().isNotEmpty().hasSize(1);
        assertThat(users.get(0).getRoles().stream().toArray(Role[]::new)).isNotEmpty().hasSize(1);
        System.out.println(users);
    }

    @Disabled
    @Test
    @DisplayName("Save Users - FAIL")
    @MongoDataFile(value = "users.json", classType = User.class, collectionName = "users")
    void whenSaveUserWithExistingUsername_thenThrowsException() {
        List<User> users = userRepository.findAll();

        User newUser = User.builder().username("newUser").email("user@mail.com").password("hasdifsdhf").version(1).build();

        userRepository.save(newUser);
    }
}