package com.javatutoriales.todo.userservice.controller;

import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.userservice.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.javatutoriales.todo.userservice.TestUtils.asJsonString;
import static com.javatutoriales.todo.userservice.TestUtils.jackson2HttpMessageConverter;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String API_URL = "/v1/users";

    @Mock
    UserService userService;

    @Spy
    UserResourceAssembler resourceAssembler;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    private UserDto user1;
    private UserDto user2;
    private UserDto user3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setMessageConverters(jackson2HttpMessageConverter())
                .build();


        user1 = UserDto.builder().id("123").username("user1").email("user1@mail.com").build();
        user2 = UserDto.builder().id("ABC").username("user2").email("user2@mail.com").version(1).build();
        user3 = UserDto.builder().id("AB123").username("user3").email("user3@mail.com").build();
    }

    @Test
    @DisplayName("GET /users - FOUND")
    public void whenGetAllUsers_ThenReturnListOfUsers() throws Exception {
        given(userService.findAll()).willReturn(Arrays.asList(user1, user2, user3));

        mockMvc.perform(get(API_URL).accept(MediaTypes.HAL_JSON_VALUE))

                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))

                .andExpect(jsonPath("$.links").exists())
                .andExpect(jsonPath("$.links").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].id", is(user1.getId())))
                .andExpect(jsonPath("$.content[0].username", is(user1.getUsername())))
                .andDo(print());

        then(userService).should(times(1)).findAll();
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("GET /users/user1 - FOUND")
    public void whenGetSingleUserByUsename_thenFoundUser() throws Exception {
        given(userService.findByUsername(user2.getUsername())).willReturn(Optional.of(user2));

        mockMvc.perform(get(API_URL + "/{username}", user2.getUsername()).accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())

                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/" + user2.getId()))

                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(user2.getId())))
                .andExpect(jsonPath("$.username", is(user2.getUsername())))
                .andExpect(jsonPath("$.email", is(user2.getEmail())))

                .andDo(print());

        then(userService).should(times(1)).findByUsername(anyString());
        then(userService).should(never()).findById(anyString());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("GET /users/CBA - NOT_FOUND")
    public void whenGetSingleUser_thenNotFound() throws Exception {
        given(userService.findByUsername(anyString())).willReturn(Optional.empty());

        mockMvc.perform(get(API_URL + "/{id}", "BCA").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(jsonPath("$").doesNotHaveJsonPath())

                .andDo(print());

        then(userService).should(times(1)).findByUsername(anyString());
        then(userService).should(never()).findById(anyString());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("POST /users - SUCCESS")
    public void whenNewUser_thenNewRecordCreated() throws Exception {

        String uuid = UUID.randomUUID().toString();
        UserDto postUser = UserDto.builder().username("new_user").name("new").lastName("user").email("new_user@mail.com").password("asdf").passwordConfirmation("asdf").build();
        UserDto mockUser = UserDto.builder().id(uuid).username("new_user").email("new_user@mail.com").version(1).build();

        given(userService.save(any())).willReturn(mockUser);

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/new_user"  ))
                .andExpect(jsonPath("$.id", is(uuid)))
                .andExpect(jsonPath("$.username", is(postUser.getUsername())))

                .andDo(print());

        then(userService).should(times(1)).save(any());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("PUT /users/ABC - SUCCESS")
    public void whenUserUpdate_thenRecordUpdated() throws Exception {
        String uuid = UUID.randomUUID().toString();

        UserDto putUser = UserDto.builder().username("updated_user").password("qwert").passwordConfirmation("qwert").name("updated").lastName("user").email("updated_user@mail.com").build();
        UserDto updatedUser = UserDto.builder().id(uuid).username("updated_user").build();
        updatedUser.setVersion(2);

        //given(userService.findById(uuid)).willReturn(Optional.of(mockUser));
        given(userService.update(eq(uuid), any())).willReturn(Optional.of(updatedUser));

        mockMvc.perform(put(API_URL + "/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(putUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"2\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/" + uuid))

                .andExpect(jsonPath("$.id", is(uuid)))
                .andExpect(jsonPath("$.username", is(putUser.getUsername())))

                .andDo(print());

        then(userService).should(never()).findById(any());
        then(userService).should(times(1)).update(anyString(), any());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("DELETE /users/ABC - SUCCESS")
    public void whenUserDelete_thenRecordDeleted() throws Exception{
        given(userService.delete(user2.getId())).willReturn(Optional.of(user2));

        mockMvc.perform(delete(API_URL + "/{id}", "ABC")
                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())

                .andDo(print());

        then(userService).should(never()).findById(user2.getId());
        then(userService).should(times(1)).delete(anyString());
        then(userService).shouldHaveNoMoreInteractions();
    }
}
