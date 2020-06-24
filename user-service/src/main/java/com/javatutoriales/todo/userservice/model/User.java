package com.javatutoriales.todo.userservice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;

    private String username;
}

