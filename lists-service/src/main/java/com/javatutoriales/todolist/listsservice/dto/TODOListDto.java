package com.javatutoriales.todolist.listsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TODOListDto {

    @Null
    private Long id;

    @NotEmpty
    private String name;

    @Null
    private Boolean complete;

    @Null
    private Integer version;

    @Null
    private Instant creationDate;
}
