package com.javatutoriales.todolist.listsservice.web.controllers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.model.PagedTodoLists;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createNewList(@RequestBody @Valid TODOListDto listDto, Principal principal) {

        log.debug("Principal: {}", principal);
        log.debug("Name: {}", principal.getName());
//        log.debug("Tasks: {}", listDto.getTasks().size());
        log.debug("Tasks: {}", listDto.getTasks());

        TODOListDto resultListDto = listService.save(listDto, principal.getName());

        return ResponseEntity
                .created(URI.create(API_URL + "/" + resultListDto.getId()))
                .eTag(String.valueOf(resultListDto.getVersion())).build();
    }

    @GetMapping
    public ResponseEntity<PagedTodoLists> getTODOLists(@RequestParam(required = false, defaultValue = "0") short page, @RequestParam(name = "results", required = false, defaultValue = "10") short resultsPerPage, Principal principal) {

        if(principal == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.debug("Principal: {}", principal);
        log.debug("Name: {}", principal.getName());


        return ResponseEntity.ok(listService.getLists(principal.getName(), page, resultsPerPage));
    }
}
