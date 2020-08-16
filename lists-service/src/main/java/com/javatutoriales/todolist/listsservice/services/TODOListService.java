package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.model.PagedTodoLists;

public interface TODOListService {
    TODOListDto save(TODOListDto listDto, String username);

    PagedTodoLists getLists(String name, int page, int resultsPerPage);
}
