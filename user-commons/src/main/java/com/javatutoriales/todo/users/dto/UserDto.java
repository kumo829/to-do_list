package com.javatutoriales.todo.users.dto;

import com.javatutoriales.todo.users.validations.PasswordsMatch;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PasswordsMatch
public class UserDto {
    @Null
    private String id;

    @Null
    private Integer version;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotEmpty
    @Size(max = 50)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String lastName;

    @NotBlank
    @Size(max = 50)
    @Email
    @Setter(AccessLevel.PRIVATE)
    private String email;


    @NotBlank
    @Size(max = 60)
    private String password;

    private boolean enabled;

    @NotBlank
    @Size(max = 60)
    private String passwordConfirmation;

    @Null
    private LocalDateTime createdDate;

    @Null
    private LocalDateTime modifiedDate;

    private Set<RoleDto> roles;
}
