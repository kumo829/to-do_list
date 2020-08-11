package com.javatutoriales.todolist.listsservice.persistence;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
class TODOListRepositoryTest {

    @Autowired
    private TODOListRepository listRepository;

    @Test
    @DisplayName("Save TODO-List - SUCCESS")
    void whenSaveTODOList_thenNewTodoListInDatabase(){

        LocalDateTime now = LocalDateTime.now();

        TODOList todoList = TODOList.builder().name("New TO-DO List").build();
        todoList.setUsername("username");

        TODOList savedList = listRepository.save(todoList);

        assertThat(savedList).isNotNull();
        assertThat(savedList.getId()).isNotNull().isPositive();
        assertThat(savedList.getCreationDate()).isNotNull().isAfterOrEqualTo(now);
        assertThat(savedList.getLastModificationDate()).isNotNull().isAfterOrEqualTo(now);
        assertThat(savedList.getVersion()).isNotNull().isEqualTo(0);
        assertThat(savedList.getVersion()).isNotNull().isEqualTo(0);
        assertThat(savedList.getUsername()).isNotNull().isEqualTo(todoList.getUsername());
        assertThat(savedList.getName()).isNotEmpty().isEqualTo(todoList.getName());
    }

    @Test
    @DisplayName("Get TODO-List paged 10 - SUCCESS")
    @DataSet("todo_lists.json")
    void whenPagedDataSetOfTen_thenGetTenSizeElementsList(){
        assertThat(listRepository.count()).isEqualTo(1000);

        Page<TODOList> firsPageResult = listRepository.findAll(PageRequest.of(0, 10));

        assertThat(firsPageResult).isNotNull();
        assertThat(firsPageResult.getTotalElements()).isEqualTo(1000);
        assertThat(firsPageResult.getTotalPages()).isEqualTo(100);
        assertThat(firsPageResult.getSize()).isEqualTo(10);

        List<TODOList> lists = firsPageResult.getContent();

        assertThat(lists).hasSize(10);
    }
}