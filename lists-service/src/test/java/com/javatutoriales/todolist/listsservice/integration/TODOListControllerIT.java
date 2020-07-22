package com.javatutoriales.todolist.listsservice.integration;


import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TODOListControllerIT {
    private static final String DOMAIN = "http://localhost";
    private static final String API_URL = "v1/todolist";

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @DisplayName("POST Save TODOList - SUCCESS")
    @Test
    void whenSaveNewTODOList_thenIdAndCreationDateShouldNotBeNull(){
        TODOListDto todoListDto = TODOListDto.builder().name("New TODOList Dto").build();

        HttpEntity<TODOListDto> request = new HttpEntity<>(todoListDto);
        final ResponseEntity<TODOListDto> listResponse = restTemplate.postForEntity(DOMAIN + ":" + port + "/" + API_URL, todoListDto, TODOListDto.class);

        assertThat(listResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(listResponse.getHeaders().getETag()).isEqualTo("\"0\"");
        assertThat(listResponse.getBody()).isNotNull();

        TODOListDto responseList = listResponse.getBody();

        assertThat(responseList).isNotNull();

    }
}
