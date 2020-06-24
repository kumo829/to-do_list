package com.javatutoriales.todo.userservice.service;

import com.javatutoriales.todo.userservice.controller.UserController;
import com.javatutoriales.todo.userservice.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.userservice.model.User;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.javatutoriales.todo.userservice.TestUtils.asJsonString;
import static com.javatutoriales.todo.userservice.TestUtils.jackson2HttpMessageConverter;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService userService;

    @Spy
    UserResourceAssembler resourceAssembler;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setMessageConverters(jackson2HttpMessageConverter())
                .build();


        user1 = User.builder().id("123").username("user1@mail.com").build();
        user2 = User.builder().id("ABC").username("user2@mail.com").build();
        user3 = User.builder().id("AB123").username("user3@mail.com").build();
    }

    @Test
    @DisplayName("GET /users - FOUND")
    public void whenGetAllUsers_ThenReturnListOfUsers() throws Exception {
        given(userService.findAll()).willReturn(Arrays.asList(user1, user2, user3));

        mockMvc.perform(get("/users").accept(MediaTypes.HAL_JSON_VALUE))

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
    @DisplayName("GET /users/ABC - FOUND")
    public void whenGetSingleUser_thenFoundUser() throws Exception {
        given(userService.findById("ABC")).willReturn(Optional.of(user2));

        mockMvc.perform(get("/users/{id}", "ABC").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())

                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.ETAG, "\"ABC\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/users/" + user2.getId()))

                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id", is(user2.getId())))
                .andExpect(jsonPath("$.username", is(user2.getUsername())))

                .andDo(print());

        then(userService).should().findById(anyString());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("GET /users/CBA - NOT_FOUND")
    public void whenGetSingleUser_thenNotFound() throws Exception {
        given(userService.findById(anyString())).willReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", "BCA").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(header().doesNotExist(HttpHeaders.CONTENT_TYPE))
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(jsonPath("$").doesNotHaveJsonPath())

                .andDo(print());

        then(userService).should().findById(anyString());
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("POST /users - SUCCESS")
    public void whenNewUser_thenNewRecordCreated() throws Exception {

        String uuid = UUID.randomUUID().toString();
        User postUser = User.builder().username("new_user").build();
        User mockUser = User.builder().id(uuid).username("new_user").build();

        given(userService.save(any())).willReturn(mockUser);

        mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(asJsonString(postUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"" + uuid + "\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/users/" + uuid))
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

        User putUser = User.builder().username("updated_user").build();
        User mockUser = new User(uuid, "updated_user");

        given(userService.findById(uuid)).willReturn(Optional.of(mockUser));
        given(userService.update(any())).willReturn(true);

        mockMvc.perform(put("/users/{id}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(putUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.LOCATION, "/users/" + uuid))
                .andExpect(jsonPath("$.id", is(uuid)))
                .andExpect(jsonPath("$.username", is(putUser.getUsername())))

                .andDo(print());

        then(userService).should(times(1)).findById(any());
        then(userService).should(times(1)).update(any());
        then(userService).shouldHaveNoMoreInteractions();
    }
}
