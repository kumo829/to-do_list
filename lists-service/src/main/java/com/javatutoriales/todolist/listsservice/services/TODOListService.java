package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;

import java.util.List;

public interface TODOListService {
    TODOListDto save(TODOListDto listDto, String username);

    List<TODOListDto> getLists(String name, int page, int resultsPerPage);
}
