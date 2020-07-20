package com.javatutoriales.todolist.listsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.time.Instant;

@Entity
@Table(name = "todo_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TODOList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private boolean complete;

    @Null
    @Version
    private Integer version;


    @CreatedDate
    @Null
    @Column(name = "creation_date", updatable = false)
    private Instant creationDate;

    @LastModifiedDate
    @Null
    @Column(name = "last_modification_date")
    private Instant lastModificationDate;
}
