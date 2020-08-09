package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;

public interface TODOListService {
    TODOListDto save(TODOListDto listDto, String username);
}
