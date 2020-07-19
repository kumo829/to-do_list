package com.javatutoriales.todolist.listsservice.web.controllers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.javatutoriales.todolist.testutils.TestUtils.asJsonString;
import static com.javatutoriales.todolist.testutils.TestUtils.jackson2HttpMessageConverter;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TODOListControllerTest {

    private static final String API_URL = "/v1/todolist";

    private MockMvc mockMvc;

    @Mock
    TODOListService listService;

    @InjectMocks
    private TODOListController listController;

    TODOListDto postListDto1 = TODOListDto.builder().name("New TO-Do List").build();
    TODOListDto mockListDto1 = TODOListDto.builder().id(1L).version(1).name(postListDto1.getName()).build();

    TODOListDto postListDto2 = TODOListDto.builder().name("Another TO-Do List").build();
    TODOListDto mockListDto2 = TODOListDto.builder().id(2L).version(1).name(postListDto1.getName()).build();

    TODOListDto postListDto3 = TODOListDto.builder().name("One Last TO-Do List").build();
    TODOListDto mockListDto3 = TODOListDto.builder().id(3L).version(1).name(postListDto1.getName()).build();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(listController)
                .setMessageConverters(jackson2HttpMessageConverter())
                .build();
    }

    @Test
    @DisplayName("POST /todolist - SUCCESS")
    public void whenNewToDoList_thenNewRecordIsCreated() throws Exception {

        given(listService.save(postListDto1)).willReturn(mockListDto1);
        given(listService.save(postListDto2)).willReturn(mockListDto2);
        given(listService.save(postListDto3)).willReturn(mockListDto3);

        performNewListDtoRequestAndValidations(mockMvc, 1, postListDto1);
        performNewListDtoRequestAndValidations(mockMvc, 2, postListDto2);
        performNewListDtoRequestAndValidations(mockMvc, 3, postListDto3);
    }

    private void performNewListDtoRequestAndValidations(@NotNull @NotNull MockMvc mockMvc, @Positive long expectedId, @NotNull TODOListDto postListDto) throws Exception {
        mockMvc.perform(post(API_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(postListDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, API_URL + "/"  + expectedId ))

                .andExpect(jsonPath("$.name", is(postListDto.getName())))
                .andExpect(jsonPath("$.complete", is(false)))

                .andDo(print());
    }
}
