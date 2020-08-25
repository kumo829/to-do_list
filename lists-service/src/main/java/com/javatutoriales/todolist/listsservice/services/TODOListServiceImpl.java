package com.javatutoriales.todolist.listsservice.services;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListMapper;
import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListSummaryDto;
import com.javatutoriales.todolist.listsservice.model.PagedTodoLists;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import com.javatutoriales.todolist.listsservice.persistence.TODOListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class TODOListServiceImpl implements TODOListService {

    private final TODOListRepository listRepository;
    private final TODOListMapper listMapper;

    @Override
    public TODOListDto save(@Validated @NotNull TODOListDto listDto, @NotNull @NotEmpty String username) {

        TODOList receivedList = listMapper.todoListDtoToTODOList(listDto);
        receivedList.setUsername(username);

        TODOList savedList = listRepository.save(receivedList);

        return listMapper.todoListToTODOListDto(savedList);
    }

    @Override
    public PagedTodoLists getLists(String name, int page, int resultsPerPage) {

        Page<TODOListSummaryDto> todoListSummaryPage = listRepository.findAllSummary(PageRequest.of(page, resultsPerPage));

        log.debug("todoListSummaryPage: {}", todoListSummaryPage);
        log.debug("todoListSummaryPage.getTotalPages(): {}", todoListSummaryPage.getTotalPages());


        return new PagedTodoLists(todoListSummaryPage.getContent(), todoListSummaryPage.getTotalPages(), todoListSummaryPage.getTotalElements());
    }
}
