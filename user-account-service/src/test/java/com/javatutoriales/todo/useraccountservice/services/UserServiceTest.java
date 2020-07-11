package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.useraccountservice.clients.UserClient;
import com.javatutoriales.todo.users.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserClient userClient;

    @InjectMocks
    UserServiceImpl userService;

    @DisplayName("Register new User - SUCCESS")
    @Test
    void whenRegisterNewUser_thenShouldReturnUserWithIdAndCreationDate(){

        LocalDateTime now = LocalDateTime.now();
        String id = UUID.randomUUID().toString();

        UserDto postUser = UserDto.builder().username("testuser").name("Test").lastName("Post User").email("testuser@test.com").password("ABC123").passwordConfirmation("ABC123").build();
        UserDto mockUser = UserDto.builder().id(id).version(1).createdDate(now).modifiedDate(now).username("testuser").name("Test").lastName("Post User").email("testuser@test.com").password("ABC123").passwordConfirmation("ABC123").build();

        given(userClient.saveUser(postUser)).willReturn(mockUser);

        UserDto newUser = userService.registerUser(postUser);

        assertThat(newUser).isNotNull();
        assertThat(newUser.getVersion()).isEqualTo(1);
        assertThat(newUser.getCreatedDate()).isAfterOrEqualTo(now);
        assertThat(newUser.getModifiedDate()).isAfterOrEqualTo(newUser.getCreatedDate());

        then(userClient).should(times(1)).saveUser(postUser);
        then(userClient).shouldHaveNoMoreInteractions();
    }
}