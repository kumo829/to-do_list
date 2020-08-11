package com.javatutoriales.todolist.listsservice.dto.mappers;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateMapper {
    public OffsetDateTime asOffsetDateTime(LocalDateTime ldt) {
        if (ldt == null) return null;

        return ldt.atOffset(ZoneOffset.UTC);
    }

    public LocalDateTime asLocalDateTime(OffsetDateTime odt) {
        if (odt == null) return null;

        return odt.toLocalDateTime();
    }
}
