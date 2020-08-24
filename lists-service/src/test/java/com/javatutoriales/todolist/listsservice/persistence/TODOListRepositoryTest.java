package com.javatutoriales.todolist.listsservice.persistence;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DBRider
class TODOListRepositoryTest {

    @Autowired
    private TODOListRepository listRepository;

    @Test
    @DisplayName("Save TODO-List - SUCCESS")
    void whenSaveTODOList_thenNewTodoListInDatabase() {

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
    @DisplayName("Save TODO-List without username - ERROR")
    void whenSaveTODOListWithoutUsername_thenConstraintViolationException(){
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            TODOList todoList = TODOList.builder().name("New TO-DO List").build();
            listRepository.save(todoList);
        });

        assertThat(exception.getConstraintViolations()).isNotNull().isNotEmpty().hasSize(1);
        List<ConstraintViolation> violations = new ArrayList<>(exception.getConstraintViolations());

        assertThat(violations.get(0).getPropertyPath().toString()).isEqualTo("username");
        assertThat(violations.get(0).getMessageTemplate()).isEqualTo("username must be provided");
    }

    @Test
    @DisplayName("Save TODO-List with expiration date - SUCCESS")
    void whenSaveTODOListWithExpirationDate_thenNewTodoListInDatabase() {

        LocalDateTime now = LocalDateTime.now();

        TODOList todoList = TODOList.builder().name("New TO-DO List").expirationDate(LocalDate.now().plusDays(10)).build();
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
        assertThat(savedList.getExpirationDate()).isNotNull().isAfterOrEqualTo(LocalDate.now().plusDays(10));
    }

    @Test
    @DisplayName("Save TODO-List with tasks - SUCCESS")
    void whenSaveTODOListWithTasks_ThenListAndTasksInDatabase() {
        LocalDate now = LocalDate.now();

        TODOList todoList = TODOList.builder().name("Test List with Tasks").build();
        List<Task> task = List.of(new Task("Task 1"), new Task("Task 2", now.plusDays(19)), new Task("Task 3"));

        todoList.setTasks(task);
        todoList.setUsername("username");

        TODOList savedList = listRepository.save(todoList);

        assertThat(savedList).isNotNull();
        assertThat(savedList.getId()).isPositive();

        assertThat(savedList.getTasks()).isNotNull().isNotEmpty().hasSize(3);
        assertThat(savedList.getTasks()).allMatch(t -> t.getId() > 0);
        assertThat(savedList.getTasks()).allMatch(t -> t.getVersion() == 0);

        List<Task> savedTasks = savedList.getTasks();

        assertAll(
                () -> assertThat(savedTasks.get(0).getExpirationDate()).isNull(),
                () -> assertThat(savedTasks.get(0).getCreationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(savedTasks.get(0).getLastModificationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now()),

                () -> assertThat(savedTasks.get(1).getExpirationDate()).isNotNull().isAfterOrEqualTo(now.plusDays(19)),
                () -> assertThat(savedTasks.get(1).getCreationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(savedTasks.get(1).getLastModificationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now()),

                () -> assertThat(savedTasks.get(2).getExpirationDate()).isNull(),
                () -> assertThat(savedTasks.get(2).getCreationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now()),
                () -> assertThat(savedTasks.get(2).getLastModificationDate()).isNotNull().isBeforeOrEqualTo(LocalDateTime.now())
        );
    }

    @Test
    @DisplayName("Get TODO-List paged 10 - SUCCESS")
    @DataSet("todo_lists.json")
    void whenPagedDataSetOfTen_thenGetTenSizeElementsList() {
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