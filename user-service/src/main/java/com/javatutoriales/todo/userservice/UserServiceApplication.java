package com.javatutoriales.todo.userservice;

import com.javatutoriales.todo.users.model.Role;
import com.javatutoriales.todo.users.model.User;
import com.javatutoriales.todo.userservice.repository.RoleRepository;
import com.javatutoriales.todo.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@EntityScan({"com.javatutoriales.todo.users.model"})
@SpringBootApplication
@EnableMongoAuditing
public class UserServiceApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = Role.builder().name("Administrator").version(1).build();
        Role roleUser = Role.builder().name("User").version(1).build();

        User adminUser = User.builder()
                .enabled(true)
                .email("amonmar2000@hotmail.com")
                .username("amontoya")
                .version(1)
                .password(passwordEncoder.encode("adminamontoya")).build();
        adminUser.addRole(roleAdmin);

        User demoUser = User.builder()
                .enabled(true)
                .email("demouser@mail.com")
                .username("demouser")
                .version(1)
                .password(passwordEncoder.encode("demouser")).build();
        demoUser.addRole(roleUser);


        roleRepository.saveAll(Arrays.asList(roleAdmin, roleUser));
        userRepository.save(adminUser);
        userRepository.save(demoUser);
    }
}
