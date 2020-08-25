package com.javatutoriales.todolist.listsservice.model;

import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListSummaryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PagedTodoLists {
    @EqualsAndHashCode.Exclude
    private final List<TODOListSummaryDto> content;

    private final int totalPages;

    private final long totalElements;
}
