package com.javatutoriales.todolist.listsservice.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListSummaryDto;
import com.javatutoriales.todolist.listsservice.model.PagedTodoLists;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import com.javatutoriales.todolist.testutils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javatutoriales.todolist.testutils.TestUtils.asJsonString;
import static com.javatutoriales.todolist.testutils.TestUtils.jackson2HttpMessageConverter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TODOListControllerTest {

    private static final String API_URL = "/v1/todolist";

    private MockMvc mockMvc;

    ObjectMapper mapper = TestUtils.getObjectMapper();

    @Mock
    TODOListService listService;

    @InjectMocks
    private TODOListController listController;

    static TODOListDto postListDto1 = TODOListDto.builder().name("New TO-Do List").build();
    static TODOListDto mockListDto1 = TODOListDto.builder().id(1L).version(0).name(postListDto1.getName()).build();

    static TODOListDto postListDto2 = TODOListDto.builder().name("Another TO-Do List").build();
    static TODOListDto mockListDto2 = TODOListDto.builder().id(2L).version(0).name(postListDto1.getName()).build();

    static TODOListDto postListDto3 = TODOListDto.builder().name("One Last TO-Do List").build();
    static TODOListDto mockListDto3 = TODOListDto.builder().id(3L).version(0).name(postListDto1.getName()).build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(listController)
                .setMessageConverters(jackson2HttpMessageConverter())
                .build();
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @MethodSource("getListsDto")
    @DisplayName("POST /todolist - SUCCESS")
    public void whenNewToDoList_thenNewRecordIsCreated(TODOListDto postListDto, TODOListDto mockList, long expectedId) throws Exception {

        given(listService.save(postListDto, "username")).willReturn(mockList);

        mockMvc.perform(
                post(API_URL)
                        .principal(getJWT("username"))
                        .contentType(MediaType.APPLICATION_JSON).content(asJsonString(postListDto)))
                .andExpect(status().isCreated())

                .andExpect(header().string(HttpHeaders.ETAG, "\"0\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/" + expectedId))

                .andDo(print());
    }


    @Test
    @DisplayName("POST /todolist - Validation ERROR")
    void whenInvalidFieldsOnTODOList_thenValidationError() throws Exception {
        TODOListDto postListDto = TODOListDto.builder().name("New TO-Do List").complete(true).id(1L).version(1).build();

        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postListDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))

                .andExpect(result -> assertAll(
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("with 3 errors")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("complete")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("version")),
                        () -> assertThat(result.getResolvedException().getMessage(), containsString("id")),

                        () -> assertThat(result.getResolvedException().getMessage(), not(containsString("name")))
                ))

                .andDo(print());
    }

    @Test
    @DisplayName("GET /todolists - Get Results with default pagination - SUCCESS")
    void whenGetResultsWithDefaultPaginationAndSorting_thenWillGetFirst10Results() throws Exception {

        TODOListDto[] lists = mapper.readValue(ClassLoader.getSystemResourceAsStream("datasets/first10Results.json"), TODOListDto[].class);

        PagedTodoLists resultList = new PagedTodoLists(Arrays.stream(lists).map(list -> new TODOListSummaryDto(list.getId(), list.getName(), list.getExpirationDate() == null ? null : list.getExpirationDate().toLocalDate(), list.getTasks() == null ? null : Long.valueOf(list.getTasks().size()), 0L, list.getCreationDate() == null ? null : list.getCreationDate().toLocalDateTime())).collect(Collectors.toList()), 1, lists.length);

        given(listService.getLists("username", 0, 10)).willReturn(resultList);

        mockMvc.perform(get(API_URL).principal(getJWT("username")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(10)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(10)))

                .andDo(print());
    }


    static Stream<Arguments> getListsDto() {
        return Stream.of(
                Arguments.of(postListDto1, mockListDto1, 1),
                Arguments.of(postListDto2, mockListDto2, 2),
                Arguments.of(postListDto3, mockListDto3, 3)
        );
    }

    private Principal getJWT(String username) {
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

        return new OAuth2Authentication(oAuth2Request, null);
    }
}
