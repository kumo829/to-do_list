package com.javatutoriales.todolist.listsservice.web.controllers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v1/todolist")
@RequiredArgsConstructor
public class TODOListController {

    private String API_URL = "/v1/todolist";

    private final TODOListService listService;

    @PostMapping
    public ResponseEntity<TODOListDto> createNewList(@RequestBody @Valid TODOListDto listDto) {

        TODOListDto resultListDto = listService.save(listDto);

        return ResponseEntity
                .created(URI.create(API_URL + "/" + resultListDto.getId()))
                .eTag(String.valueOf(resultListDto.getVersion()))
                .body(listDto);
    }
}
