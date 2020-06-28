package com.javatutoriales.todo.userservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private int version;

    @NotBlank
    @Size(max = 50)
    @Indexed(unique = true, name = "user_username_index_unique")
    @Setter(AccessLevel.PRIVATE)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @Setter(AccessLevel.PRIVATE)
    @Indexed(unique = true, name = "user_email_index_unique", useGeneratedName = true)
    private String email;

    @NotBlank
    @Size(max = 60)
    private String password;

    private boolean enabled;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @DBRef
    private Set<Role> roles;

    public User(String id, @NotBlank @Size(max = 20) String username) {
        this.id = id;
        this.username = username;
    }

    public void addRole(@NotNull Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }

        roles.add(role);
    }
}

