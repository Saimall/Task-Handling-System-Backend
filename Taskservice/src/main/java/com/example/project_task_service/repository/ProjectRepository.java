package com.example.project_task_service.repository;

import com.example.project_task_service.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findAllByManagerId(Long managerId);
}
