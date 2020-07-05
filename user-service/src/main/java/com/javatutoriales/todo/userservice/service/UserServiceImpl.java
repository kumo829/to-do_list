package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.users.model.Role;
import com.javatutoriales.todo.users.model.User;
import com.javatutoriales.todo.userservice.repository.RoleRepository;
import com.javatutoriales.todo.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String CLIENT_ROLE_NAME = "User";

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        user.setVersion(1);

        Optional<Role> clientRole = roleRepository.findByName(CLIENT_ROLE_NAME);

        return clientRole.map(role -> {
            user.addRole(role);
            user.setEnabled(true); //TODO: Temporal enabling on enroll, will be replaced with email confirmation
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }).orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<User> update(String userId, User user) {
        Optional<User> existingUser = this.findById(userId);

        return existingUser.map(u -> {
            u.setPassword(user.getPassword());
            u.setVersion(user.getVersion() + 1);
            return Optional.of(userRepository.save(u));
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<User> delete(String userId) {
        Optional<User> existingUser = this.findById(userId);

        return existingUser.map(user -> {
            userRepository.deleteById(userId);
            return existingUser;
        }).orElse(Optional.empty());
    }
}
