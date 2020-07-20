package com.javatutoriales.todolist.listsservice.persistence;

import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TODOListRepository extends PagingAndSortingRepository<TODOList, Long> {
}
