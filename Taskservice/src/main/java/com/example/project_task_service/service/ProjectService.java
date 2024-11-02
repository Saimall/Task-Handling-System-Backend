package com.example.project_task_service.service;

import com.example.project_task_service.dto.ProjectDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.repository.ProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project addProject(ProjectDto projectDto, Long managerId) {
        Project project = Project.builder()
                .projectName(projectDto.getProjectName())
                .projectDescription(projectDto.getProjectDescription())
                .startDate(projectDto.getStartDate())
                .endDate(projectDto.getEndDate())
                .managerId(managerId)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
        return projectRepository.save(project);
    }

    public List<ProjectDto> getProjectsByManager(Long managerId) {
        List<Project> projects = projectRepository.findAllByManagerId(managerId);

        if (projects.isEmpty()) {
            throw new ProjectNotFoundException("No projects found for manager with ID: " + managerId);
        }

        return projects.stream().map(project -> {
            ProjectDto projectDto = new ProjectDto();
            BeanUtils.copyProperties(project, projectDto);
            return projectDto;
        }).toList();
    }

    public String deleteProjectsByManager(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            projectRepository.deleteById(projectId);
            return "Project deleted successfully with id: " + projectId;
        } else {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
    }
}
