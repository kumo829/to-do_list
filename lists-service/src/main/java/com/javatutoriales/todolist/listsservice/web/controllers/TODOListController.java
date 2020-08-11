package com.javatutoriales.todolist.listsservice.web.controllers;

import com.javatutoriales.todolist.listsservice.dto.TODOListDto;
import com.javatutoriales.todolist.listsservice.services.TODOListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/todolist")
@RequiredArgsConstructor
@Slf4j
public class TODOListController {

    private String API_URL = "/v1/todolist";

    private final TODOListService listService;

    @PostMapping
    public ResponseEntity createNewList(@RequestBody @Valid TODOListDto listDto, Principal principal) {

        log.debug("Principal: {}", principal);
        log.debug("Name: {}", principal.getName());

        TODOListDto resultListDto = listService.save(listDto, principal.getName());

        return ResponseEntity
                .created(URI.create(API_URL + "/" + resultListDto.getId()))
                .eTag(String.valueOf(resultListDto.getVersion())).build();
    }

    @GetMapping
    public ResponseEntity<List<TODOListDto>> getTODOLists(@RequestParam(required = false, defaultValue = "0") short page, @RequestParam(required = false, defaultValue = "10") short resultsPerPage, Principal principal) {
        log.debug("Principal: {}", principal);
        log.debug("Name: {}", principal.getName());

        List<TODOListDto> result = listService.getLists(principal.getName(), page, resultsPerPage);

        return ResponseEntity.ok(result);
    }
}
