package com.javatutoriales.todo.useraccountservice.web.controller;

import com.javatutoriales.todo.useraccountservice.services.UserService;
import com.javatutoriales.todo.useraccountservice.web.controller.hateoas.UserResourceAssembler;
import com.javatutoriales.todo.users.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.javatutoriales.todolist.testutils.TestUtils.asJsonString;
import static com.javatutoriales.todolist.testutils.TestUtils.jackson2HttpMessageConverter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {

    private static final String API_URL = "/v1/register";

    @Mock
    UserService userService;

    @Spy
    UserResourceAssembler resourceAssembler;

    @InjectMocks
    RegistrationController registrationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController)
                .setMessageConverters(jackson2HttpMessageConverter())
                .build();
    }

    @Test
    @DisplayName("POST /v1/register - SUCCESS")
    void whenNewUser_thenNewRecordIsCreated() throws Exception {
        UserDto postUser = UserDto.builder().username("testuser").name("Test").lastName("Post User").email("testuser@test.com").password("ABC123").passwordConfirmation("ABC123").build();
        UserDto mockUser = UserDto.builder().username("testuser").name("Test").lastName("Post User").email("testuser@test.com").password("ABC123").passwordConfirmation("ABC123").version(1).build();

        given(userService.registerUser(postUser)).willReturn(mockUser);

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/" + postUser.getUsername()))

                .andExpect(jsonPath("$.username", is(postUser.getUsername())))

                .andDo(print());

        then(userService).should(times(1)).registerUser(postUser);
        then(userService).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("POST /v1/register - ERROR, fields are required")
    void whenNoRequiredFieldsProvided_thenReturnError() throws Exception {
        UserDto postUser = UserDto.builder().build();

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postUser)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))

                .andExpect(result -> assertAll(
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("with 6 errors")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("username")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("password")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("passwordConfirmation")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("name")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("lastName")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("email")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("email")),
                        () -> assertThat(result.getResolvedException().getMessage(), not(containsString("version"))),
                        () -> assertThat(result.getResolvedException().getMessage(), not(containsString("enabled"))),
                        () -> assertThat(result.getResolvedException().getMessage(), not(containsString("createdDate"))),
                        () -> assertThat(result.getResolvedException().getMessage(), not(containsString("modifiedDate")))
                ))

                .andDo(print());
    }

    @Test
    @DisplayName("POST /v1/register - ERROR, passwords don't match")
    void whenNoPasswordConfirmationMatch_thenReturnError() throws Exception {
        UserDto postUser = UserDto.builder().username("testuser").name("Test").lastName("Post User").email("testuser@test.com").password("ABC123").passwordConfirmation("abc123").build();

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postUser)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))

                .andExpect(result -> assertThat(result.getResolvedException().getMessage(), containsString("Passwords do not match")))

                .andDo(print());

    }
}