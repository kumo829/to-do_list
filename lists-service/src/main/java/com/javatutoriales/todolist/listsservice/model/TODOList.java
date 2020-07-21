package com.javatutoriales.todolist.listsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Entity
@Table(name = "todo_lists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TODOList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private boolean complete;

    @Version
    private Integer version;


    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    private Instant creationDate;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    private Instant lastModificationDate;
}
