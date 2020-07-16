package com.javatutoriales.todo.useraccountservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Document(collection = "verification_tokens")
@Data
@RequiredArgsConstructor
public class Verification {
    @Id
    private String id;

    @Indexed(unique = true)
    private final String username;

    private boolean used = false;

    @CreatedDate
    @Null
    private LocalDateTime createdDate;

    private LocalDateTime activationDate;
}
