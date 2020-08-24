package com.javatutoriales.todolist.listsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {
    @Null
    private Long id;

    @NotEmpty
    private String name;

    @Null
    private Boolean complete;

    private LocalDate expiration;

    public TaskDto(@NotEmpty String name) {
        this.name = name;
    }

    public TaskDto(@NotEmpty String name, LocalDate expiration) {
        this.name = name;
        this.expiration = expiration;
    }
}
