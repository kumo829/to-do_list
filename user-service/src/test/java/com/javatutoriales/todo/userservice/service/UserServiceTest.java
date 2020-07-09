package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.mappers.UserMapper;
import com.javatutoriales.todo.users.model.User;
import com.javatutoriales.todo.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    private User user1;
    private User user2;
    private UserDto userDto2;
    private User user3;

    @BeforeEach
    public void setUp(){
        user1 = User.builder().id("123").username("user1@mail.com").version(1).build();
        user2 = User.builder().id("ABC").username("user2@mail.com").version(1).build();
        userDto2 = UserDto.builder().id("ABC").username("user2@mail.com").version(1).build();
        user3 = User.builder().id("AB123").username("user3@mail.com").build();
    }

    @Test
    @DisplayName("Find All")
    void whenFindAllUsers_themUserListHasThreeElements() {
        given(userRepository.findAll()).willReturn(Arrays.asList(user1, user2, user3));

        Iterable<UserDto> users = userService.findAll();

        assertThat(users).isNotNull().isNotEmpty().hasSize(3);

        then(userRepository).should(times(1)).findAll();
        then(userRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Find by Id")
    void whenFindById_thenShouldReturnUser2() {
        given(userRepository.findById("ABC")).willReturn(Optional.of(user2));
        given(userMapper.userToUserDto(user2)).willReturn(userDto2);

        Optional<UserDto> user = userService.findById("ABC");

        assertThat(user).isPresent().isNotNull();
        assertThat(user.get().getUsername()).isEqualTo("user2@mail.com");
        assertThat(user.get().getVersion()).isEqualTo(1);

        then(userRepository).should(times(1)).findById(anyString());
        then(userRepository).shouldHaveNoMoreInteractions();
    }
}