package com.javatutoriales.todolist.listsservice.dto.mappers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class, DateMapper.class})
public interface TODOListMapper {
    TODOListDto todoListToTODOListDto(TODOList todoList);

    TODOList todoListDtoToTODOList(TODOListDto todoListDto);
}
