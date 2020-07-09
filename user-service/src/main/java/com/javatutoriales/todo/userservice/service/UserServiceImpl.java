package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.mappers.UserMapper;
import com.javatutoriales.todo.users.model.Role;
import com.javatutoriales.todo.users.model.User;
import com.javatutoriales.todo.userservice.repository.RoleRepository;
import com.javatutoriales.todo.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final String CLIENT_ROLE_NAME = "User";

    @Override
    public Iterable<UserDto> findAll() {
        return userRepository.findAll().stream().map(user -> userMapper.userToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {

        return userRepository.findByUsername(username).map(user -> {
                    log.info("User: {}", user);
                    log.info("Mapped userDto: {}", userMapper.userToUserDto(user));
                    return Optional.of(userMapper.userToUserDto(user));
                }
        ).orElse(Optional.empty());
    }

    @Override
    public Optional<UserDto> findById(String id) {
        return userRepository.findById(id).map(user -> {
                    return Optional.of(userMapper.userToUserDto(user));
                }
        ).orElse(Optional.empty());
    }

    @Override
    public UserDto save(UserDto userDto) {
        User user = userMapper.usetDtoToUser(userDto);
//        user.setVersion(1);

        Optional<Role> clientRole = roleRepository.findByName(CLIENT_ROLE_NAME);

        return clientRole.map(role -> {
            user.addRole(role);
            user.setEnabled(true); //TODO: Temporal enabling on enroll, will be replaced with email confirmation
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userMapper.userToUserDto(userRepository.save(user));
        }).orElseThrow(() -> new IllegalStateException("Client role not found"));
    }

    @Override
    public Optional<UserDto> update(String userId, UserDto userDto) {
        Optional<UserDto> existingUser = this.findById(userId);


        return existingUser.map(u -> {
            User user = userMapper.usetDtoToUser(u);
            u.setPassword(user.getPassword());
            u.setVersion(user.getVersion() + 1);
            User savedUser = userRepository.save(user);
            return Optional.of(userMapper.userToUserDto(savedUser));
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<UserDto> delete(String userId) {
        Optional<UserDto> existingUser = this.findById(userId);

        return existingUser.map(user -> {
            userRepository.deleteById(userId);
            return existingUser;
        }).orElse(Optional.empty());
    }
}
