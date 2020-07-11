package com.javatutoriales.todo.useraccountservice.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todolist.testutils.wiremock.LocalRibbonClientConfiguration;
import com.javatutoriales.todolist.testutils.wiremock.WireMockInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.javatutoriales.todolist.testutils.TestUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {WireMockInitializer.class}, classes = {LocalRibbonClientConfiguration.class})
public class RegistrationControllerIT {

    private static final String DOMAIN = "http://localhost";
    private static final String API_URL = "/v1/register";

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @AfterEach
    public void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    @DisplayName("POST register user - SUCCESS")
    void whenRegisterNewUser_thenUserHasIs() {

        UserDto postUser = UserDto.builder().username("newPostUser").name("new").lastName("user").email("postuser@mail.com").password("avc123").passwordConfirmation("avc123").build();
        LocalDateTime now = LocalDateTime.now();

        UserDto userReponse = UserDto.builder().id(UUID.randomUUID().toString()).username("newPostUser").name("new").lastName("user").email("postuser@mail.com").password("avc123").passwordConfirmation("avc123").version(1).createdDate(now).modifiedDate(now).build();

        wireMockServer.givenThat(WireMock.post("/v1/users/")
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(asJsonString(userReponse)))
        );

        HttpEntity<UserDto> request = new HttpEntity<>(postUser);
        final ResponseEntity<EntityModel<UserDto>> userResponse = restTemplate.exchange(DOMAIN + ":" + port + "/" + API_URL, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });

        assertThat(userResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(userResponse.getHeaders().getETag()).isEqualTo("\"1\"");

        assertThat(userResponse.getBody()).isNotNull();

        UserDto user = userResponse.getBody().getContent();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull().isNotEmpty().isNotBlank();
        assertThat(user.getCreatedDate()).isAfterOrEqualTo(now);
        assertThat(user.getModifiedDate()).isAfterOrEqualTo(now);
        assertThat(user.getUsername()).isEqualTo("newPostUser");
    }


}
