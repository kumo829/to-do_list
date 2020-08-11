package com.javatutoriales.todolist.listsservice.persistence;

import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TODOListRepository extends JpaRepository<TODOList, Long> {
}
