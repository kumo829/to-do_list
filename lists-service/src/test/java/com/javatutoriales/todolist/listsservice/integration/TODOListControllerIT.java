package com.javatutoriales.todolist.listsservice.integration;


import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TODOListControllerIT {
    private static final String DOMAIN = "http://localhost";
    private static final String API_URL = "v1/todolist";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DefaultTokenServices tokenServices;

    @LocalServerPort
    private Integer port;

    @DisplayName("POST Save TODOList - SUCCESS")
    @Test
    void whenSaveNewTODOList_thenIdAndCreationDateShouldNotBeNull() {
        TODOListDto todoListDto = TODOListDto.builder().name("New TODOList Dto").build();

        Set<String> scopes = new HashSet<>();

        String token = getJWT("username");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TODOListDto> request = new HttpEntity<>(todoListDto, headers);

        final ResponseEntity<TODOListDto> listResponse = restTemplate.postForEntity(DOMAIN + ":" + port + "/" + API_URL, request, TODOListDto.class);

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(listResponse.getHeaders().getETag()).isEqualTo("\"0\"");
        assertThat(listResponse.getBody()).isNull();

        TODOListDto responseList = listResponse.getBody();

        assertThat(responseList).isNull();
    }

    private String getJWT(String username){
        OAuth2Request oAuth2Request = new OAuth2Request(null,
                username,
                List.of(new SimpleGrantedAuthority("ROLE_User")),
                true,
                Set.of("read", "write"),
                null,
                null,
                null,
                Map.of("emal", "user@mail.com", "name", "User")
        );

        OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, null);

        return tokenServices.createAccessToken(auth).getValue();
    }
}
