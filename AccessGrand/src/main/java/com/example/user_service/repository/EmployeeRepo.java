package com.example.user_service.repository;

import com.example.user_service.model.Employee;
import com.example.user_service.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee,Long> {
    List<Employee> findByManager(Manager manager);

    boolean existsByEmail(String email);

    Employee findByEmail(String email);
}
