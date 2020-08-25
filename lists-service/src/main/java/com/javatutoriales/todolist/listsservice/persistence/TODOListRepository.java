package com.javatutoriales.todolist.listsservice.persistence;

import com.javatutoriales.todolist.listsservice.dto.mappers.TODOListSummaryDto;
import com.javatutoriales.todolist.listsservice.model.TODOList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TODOListRepository extends JpaRepository<TODOList, Long> {
    @Query(value = "SELECT new com.javatutoriales.todolist.listsservice.dto.mappers.TODOListSummaryDto(tdl.id, tdl.name, tdl.expirationDate, count(t), (SELECT count(ts) FROM TODOList tdls LEFT OUTER JOIN tdls.tasks ts WHERE ts.complete = true AND tdls = tdl GROUP BY tdls.id), tdl.creationDate) FROM TODOList tdl LEFT OUTER JOIN tdl.tasks t GROUP BY tdl.id",
            countQuery = "SELECT count(tdl) FROM TODOList tdl")
    Page<TODOListSummaryDto> findAllSummary(Pageable pageable);
}
