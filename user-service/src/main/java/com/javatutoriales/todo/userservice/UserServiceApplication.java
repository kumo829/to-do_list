package com.javatutoriales.todo.userservice;

import com.javatutoriales.todo.userservice.repository.RoleRepository;
import com.javatutoriales.todo.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EntityScan({"com.javatutoriales.todo.users.model"})
@SpringBootApplication
@EnableMongoAuditing
public class UserServiceApplication{

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
