package com.example.project_task_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate startDate;
    private LocalDate endDate;
}
