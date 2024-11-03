package com.example.project_task_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDashboardDto {
    private String name;
    private String email;
    private String contact;
    private String designation;
}
