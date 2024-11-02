package com.example.project_task_service.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manager {
    private Long managerId;
    private String name;
    private String email;
    private String contact;

    private List<Employee> employees;
}
