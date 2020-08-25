package com.javatutoriales.todolist.listsservice.dto.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TODOListSummaryDto {

    private long id;

    private String name;

    private LocalDate expirationDate;

    private Long numberOfTask;

    private Long numberOfCompletedTasks;

    private LocalDateTime creationDate;
}
