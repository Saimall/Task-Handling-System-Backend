package com.example.project_task_service.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.project_task_service.dto.ProjectDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private ProjectDto projectDto;
    private Project project;
    
    @BeforeEach
    void setUp() {
        projectDto = new ProjectDto();
        projectDto.setProjectName("New Project");
        projectDto.setProjectDescription("Project Description");
        projectDto.setStartDate(LocalDate.now());
        projectDto.setEndDate(LocalDate.now().plusMonths(1));

        project = Project.builder()
                .projectId(1L)
                .projectName("New Project")
                .projectDescription("Project Description")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .managerId(1L)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    @Test
    void testAddProject_Success() {
        Long managerId = 1L;

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.addProject(projectDto, managerId);

        assertNotNull(createdProject);
        assertEquals("New Project", createdProject.getProjectName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testGetProjectsByManager_Success() {
        Long managerId = 1L;

        when(projectRepository.findAllByManagerId(managerId)).thenReturn(Collections.singletonList(project));

        List<ProjectDto> projectDtos = projectService.getProjectsByManager(managerId);

        assertNotNull(projectDtos);
        assertEquals(1, projectDtos.size());
        assertEquals("New Project", projectDtos.get(0).getProjectName());
        verify(projectRepository, times(1)).findAllByManagerId(managerId);
    }

    @Test
    void testGetProjectsByManager_NoProjects() {
        Long managerId = 1L;

        when(projectRepository.findAllByManagerId(managerId)).thenReturn(Collections.emptyList());

        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProjectsByManager(managerId);
        });

        assertEquals("No projects found for manager with ID: 1", exception.getMessage());
    }

    @Test
    void testDeleteProjectsByManager_Success() {
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        String result = projectService.deleteProjectsByManager(projectId);

        assertEquals("Project deleted successfully with id: 1", result);
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void testDeleteProjectsByManager_ProjectNotFound() {
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ProjectNotFoundException exception = assertThrows(ProjectNotFoundException.class, () -> {
            projectService.deleteProjectsByManager(projectId);
        });

        assertEquals("Project not found with id: 1", exception.getMessage());
    }
}
