package com.example.project_task_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long managerId;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    @OneToMany(mappedBy ="project", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)
    @JsonManagedReference
    private List<Task> tasks;
}
