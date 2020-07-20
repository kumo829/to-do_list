package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListMapper;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.persistence.TODOListRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

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

    @Test
    @DisplayName("Register new TODO-List - SUCCESS")
    void whenSaverNewTODOList_thenNewTODOListWithIdAndCreationDate() {
        Instant now = Instant.now();
        TODOListDto listDto = TODOListDto.builder().name("New TODO List").build();
        TODOList responseList = TODOList.builder().creationDate(now).lastModificationDate(now).id(1L).name(listDto.getName()).version(1).build();
        TODOListDto mockList = TODOListDto.builder().creationDate(now).id(1L).name(listDto.getName()).version(1).complete(false).build();

        given(listMapper.todoListDtoToTODOList(any(TODOListDto.class))).willReturn(responseList);
        given(listMapper.todoListToTODOListDto(any(TODOList.class))).willReturn(mockList);


        given(listRepository.save(any(TODOList.class))).willReturn(responseList);

        TODOListDto savedList = listService.save(listDto);

        assertThat(savedList).isNotNull();
        assertThat(savedList.getName()).isNotEmpty();
        assertThat(savedList.getName()).isEqualTo(listDto.getName());
        assertThat(savedList.getComplete()).isFalse();
        assertThat(savedList.getId()).isNotNull().isPositive();
        assertThat(savedList.getVersion()).isNotNull().isEqualTo(1);
        assertThat(savedList.getCreationDate()).isNotNull().isAfterOrEqualTo(now);

        then(listMapper).should(times(1)).todoListDtoToTODOList(any(TODOListDto.class));
        then(listMapper).should(times(1)).todoListToTODOListDto(any(TODOList.class));
        then(listMapper).shouldHaveNoMoreInteractions();
        then(listRepository).should(times(1)).save(any(TODOList.class));
        then(listRepository).shouldHaveNoMoreInteractions();
    }
}