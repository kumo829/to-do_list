package com.javatutoriales.todolist.listsservice.web.controllers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/v1/todolist")
@RequiredArgsConstructor
@Slf4j
public class TODOListController {

    private String API_URL = "/v1/todolist";

    private final TODOListService listService;

    @PostMapping
    public ResponseEntity<TODOListDto> createNewList(@RequestBody @Valid TODOListDto listDto, Principal principal) {

        log.info("Principal: {}", principal);
        log.info("Name: {}", principal.getName());

        TODOListDto resultListDto = listService.save(listDto, principal.getName());

        return ResponseEntity
                .created(URI.create(API_URL + "/" + resultListDto.getId()))
                .eTag(String.valueOf(resultListDto.getVersion()))
                .body(listDto);
    }
}
