package com.example.project_task_service.dto;

import com.example.project_task_service.model.Priority;
import com.example.project_task_service.model.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {
    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private Long employeeId;
    private LocalDateTime dueDateTime;
    @Enumerated(value = EnumType.STRING)
    private Priority priority;
    @Enumerated(value = EnumType.STRING)
    private Status status;
}
