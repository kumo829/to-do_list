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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    private LocalDate expirationDate;

    @CreatedDate
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "last_modification_date")
    private LocalDateTime lastModificationDate;

    @Version
    int version;

    private boolean complete;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    public Task(String name) {
        this.name = name;
    }

    public Task(String name, LocalDate expirationDate) {
        this(name);
        this.expirationDate = expirationDate;
    }
}