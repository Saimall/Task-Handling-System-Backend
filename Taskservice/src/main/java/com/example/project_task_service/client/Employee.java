package com.example.project_task_service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private Long empId;
    private String name;
    private String email;
    private String contact;
    private String designation;
}
