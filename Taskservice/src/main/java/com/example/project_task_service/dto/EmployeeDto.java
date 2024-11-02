package com.example.project_task_service.dto;

import com.example.project_task_service.client.Manager;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {
	private Long empId;
    private String name;
    private String email;
    private String contact;
    private String designation;
    private Manager manager;

}
