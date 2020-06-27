package com.javatutoriales.todo.userservice.integration;

import com.javatutoriales.todo.userservice.model.User;
import com.javatutoriales.todo.userservice.repository.mongo.MongoDataFile;
import com.javatutoriales.todo.userservice.repository.mongo.MongoSpringExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MongoSpringExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String DOMAIN = "http://localhost";
    private static final String API_URL = "/api/v1/users";

    @LocalServerPort
    private int port;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET all Users - SUCCESS")
    @MongoDataFile(value = "users.json", classType = User.class, collectionName = "users")
    public void whenGetAllUsers_thenGetFourUserObjects() {

        LocalDateTime now = LocalDateTime.now();

//        System.out.println(restTemplate.getForEntity("http://localhost:" + port + "/" + API_URL, Object.class));

        final ResponseEntity<CollectionModel<User>> usersResponse = restTemplate
                .exchange(DOMAIN + ":" + port + "/" + API_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(usersResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);

        assertThat(usersResponse.getBody()).isNotNull().isNotEmpty();
        assertThat(usersResponse.getBody().getContent()).isNotNull().isNotEmpty().hasSize(4);

        List<User> userList = new ArrayList<>(usersResponse.getBody().getContent());//.stream().map(element -> element).collect(Collectors.toList());

        assertThat(userList.get(0)).isNotNull();
        assertThat(userList.get(0).getId()).isEqualTo("e140057d-a6b6-437c-89be-8ab7b7a25572");
        assertThat(userList.get(0).getUsername()).isEqualTo("newUser");
        assertThat(userList.get(0).getEmail()).isEqualTo("user@mail.com");
        assertThat(userList.get(0).getCreatedDate()).isBefore(now);
    }

    @Test
    @DisplayName("GET Single User By Username - SUCCESS")
    @MongoDataFile(value = "users.json", classType = User.class, collectionName = "users")
    public void whenFindSingleUserByUsername_thenGetUser() {
        final ResponseEntity<EntityModel<User>> userResponse = restTemplate
                .exchange(DOMAIN + ":" + port + "/" + API_URL + "/" + "otherUser", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        assertThat(userResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(userResponse.getHeaders().getETag()).isEqualTo("\"3\"");

        assertThat(userResponse.getBody()).isNotNull();

        User user = userResponse.getBody().getContent();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("cdab060e-3bf8-4187-ba12-8b27fb1b4aed");
        assertThat(user.getEmail()).isEqualTo("otheruser@mail.com");
    }

    @Test
    @DisplayName("GET Non existing user - Not Found")
    @MongoDataFile(value = "users.json", classType = User.class, collectionName = "users")
    public void whenFindSingleUserByUsername_thenNotFound() {
        final ResponseEntity<EntityModel<User>> userResponse = restTemplate
                .exchange(DOMAIN + ":" + port + "/" + API_URL + "/" + "nonExisting", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        assertThat(userResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
        assertThat(userResponse.getBody()).isNull();
    }

    @Test
    @DisplayName("POST Add new User - SUCCESS")
    public void whenAddNewUser_thenOneUserOnDatabase() {
        User postUser = User.builder().username("newPostUser").username("postuser@mail.com").password("avc123").build();
        LocalDateTime now = LocalDateTime.now();

        HttpEntity<User> request = new HttpEntity<>(postUser);
        final ResponseEntity<EntityModel<User>> userResponse = restTemplate.exchange(DOMAIN + ":" + port + "/" + API_URL, HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });

        assertThat(userResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(userResponse.getHeaders().getETag()).isEqualTo("\"1\"");
        assertThat(userResponse.getBody()).isNotNull();

        User user = userResponse.getBody().getContent();
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull().isNotEmpty().isNotBlank();
        assertThat(user.getCreatedDate()).isAfter(now);
        assertThat(user.getModifiedDate()).isAfter(now);

        User dbUser = mongoTemplate.findById(user.getId(), User.class);
        assertThat(dbUser).isNotNull();
        assertThat(dbUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(dbUser.getPassword()).isEqualTo(user.getPassword());
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
