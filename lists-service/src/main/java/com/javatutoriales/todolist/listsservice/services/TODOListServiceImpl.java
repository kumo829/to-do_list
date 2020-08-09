package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListMapper;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.persistence.TODOListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class TODOListServiceImpl implements TODOListService {

    private final TODOListRepository listRepository;
    private final TODOListMapper listMapper;

    @Override
    public TODOListDto save(@Validated TODOListDto listDto, @NotNull @NotEmpty String username) {

        listDto.setComplete(false);

        TODOList receivedList = listMapper.todoListDtoToTODOList(listDto);
        receivedList.setUsername(username);

        TODOList savedList = listRepository.save(receivedList);

        return listMapper.todoListToTODOListDto(savedList);
    }
}
