package com.javatutoriales.todolist.listsservice.persistence;

import com.github.database.rider.junit5.DBUnitExtension;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.ConnectionHandle;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
class TODOListRepositoryTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private TODOListRepository listRepository;

    public ConnectionHandle getConnectionHolder() throws SQLException {
        return (ConnectionHandle)dataSource.getConnection();
    }

    @Test
    @DisplayName("Save TODO-List - SUCCESS")
    void whenSaveTODOList_thenNewTodoListInDatabase(){

        Instant now = Instant.now();

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

}