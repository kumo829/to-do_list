package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListMapper;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.persistence.TODOListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
public class TODOListServiceImpl implements TODOListService {

    private final TODOListRepository listRepository;
    private final TODOListMapper listMapper;

    @Override
    public TODOListDto save(@Validated TODOListDto listDto) {

        listDto.setComplete(false);

        TODOList savedList = listRepository.save(listMapper.todoListDtoToTODOList(listDto));

        return listMapper.todoListToTODOListDto(savedList);
    }
}
