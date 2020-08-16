package com.javatutoriales.todolist.listsservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListMapper;
import com.javatutoriales.todolist.listsservice.model.PagedTodoLists;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.persistence.TODOListRepository;
import com.javatutoriales.todolist.testutils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TODOListServiceTest {

    @Mock
    TODOListRepository listRepository;

    @Mock
    TODOListMapper listMapper;

    @InjectMocks
    TODOListServiceImpl listService;

    @Spy
    TODOListMapper realListMapper;


    @Test
    @DisplayName("Register new TODO-List - SUCCESS")
    void whenSaverNewTODOList_thenNewTODOListWithIdAndCreationDate() {
        LocalDateTime now = LocalDateTime.now();
        OffsetDateTime offNow = OffsetDateTime.now();
        TODOListDto listDto = TODOListDto.builder().name("New TODO List").build();
        TODOList responseList = TODOList.builder().creationDate(now).lastModificationDate(now).id(1L).name(listDto.getName()).version(1).build();
        TODOListDto mockList = TODOListDto.builder().creationDate(offNow).id(1L).name(listDto.getName()).version(1).complete(false).build();

        given(listMapper.todoListDtoToTODOList(any(TODOListDto.class))).willReturn(responseList);
        given(listMapper.todoListToTODOListDto(any(TODOList.class))).willReturn(mockList);


        given(listRepository.save(any(TODOList.class))).willReturn(responseList);

        TODOListDto savedList = listService.save(listDto, "username");

        assertThat(savedList).isNotNull();
        assertThat(savedList.getName()).isNotEmpty();
        assertThat(savedList.getName()).isEqualTo(listDto.getName());
        assertThat(savedList.getComplete()).isFalse();
        assertThat(savedList.getId()).isNotNull().isPositive();
        assertThat(savedList.getVersion()).isNotNull().isEqualTo(1);
        assertThat(savedList.getCreationDate()).isNotNull().isAfterOrEqualTo(offNow);

        then(listMapper).should(times(1)).todoListDtoToTODOList(any(TODOListDto.class));
        then(listMapper).should(times(1)).todoListToTODOListDto(any(TODOList.class));
        then(listMapper).shouldHaveNoMoreInteractions();
        then(listRepository).should(times(1)).save(any(TODOList.class));
        then(listRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Get all TODO-List Paginated - SUCCESS")
    void whenGetTODOListPaginated_thenListMustContainFirst10Elements() throws IOException {

        ObjectMapper mapper = TestUtils.getObjectMapper();

        TODOListDto[] lists = mapper.readValue(ClassLoader.getSystemResourceAsStream("datasets/first10Results.json"), TODOListDto[].class);

        List<TODOList> todoLists = Arrays.stream(lists).map(list -> realListMapper.todoListDtoToTODOList(list)).collect(Collectors.toList());

        Page<TODOList> todoListPage = new PageImpl<>(todoLists);

        given(listRepository.findAll(any(Pageable.class))).willReturn(todoListPage);


        PagedTodoLists result = listService.getLists("username", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getContent()).isNotNull().isNotEmpty().hasSize(10);
    }
}