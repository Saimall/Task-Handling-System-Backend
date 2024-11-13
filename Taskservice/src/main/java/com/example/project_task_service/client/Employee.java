package com.example.project_task_service.client;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long empId;
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String contact;
    private String designation;
}
