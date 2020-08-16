package com.javatutoriales.todolist.listsservice.dto.mappers;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class DateMapper {
    public OffsetDateTime asOffsetDateTime(LocalDate ld) {
        if (ld == null) return null;

        return ld.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    public LocalDate asLocalDateTime(OffsetDateTime odt) {
        if (odt == null) return null;

        return odt.toLocalDate();
    }
}
