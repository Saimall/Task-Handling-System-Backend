package com.example.project_task_service.controller;

import com.example.project_task_service.dto.ProjectDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.service.ProjectService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddProject() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectName("New Project");
        Long managerId = 1L;

        Project newProject = new Project();
        newProject.setProjectId(1L);
        newProject.setProjectName("New Project");

        when(projectService.addProject(any(ProjectDto.class), eq(managerId))).thenReturn(newProject);

        mockMvc.perform(post("/api/v2/project/addProject/{managerId}", managerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value(newProject.getProjectId()))
                .andExpect(jsonPath("$.projectName").value(newProject.getProjectName()));

        verify(projectService, times(1)).addProject(any(ProjectDto.class), eq(managerId));
    }

    @Test
    void testGetProjectsByManager_WithProjects() throws Exception {
        Long managerId = 1L;
        ProjectDto projectDto = new ProjectDto();
        projectDto.setProjectName("Project 1");

        List<ProjectDto> projects = Collections.singletonList(projectDto);
        when(projectService.getProjectsByManager(managerId)).thenReturn(projects);

        mockMvc.perform(get("/api/v2/project/getProjects/{managerId}", managerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].projectName").value("Project 1"));

        verify(projectService, times(1)).getProjectsByManager(managerId);
    }

    @Test
    void testGetProjectsByManager_NoProjects() throws Exception {
        Long managerId = 1L;
        when(projectService.getProjectsByManager(managerId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/project/getProjects/{managerId}", managerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No projects found under this manager"));

        verify(projectService, times(1)).getProjectsByManager(managerId);
    }

    @Test
    void testGetProjectsByManager_ProjectNotFoundException() throws Exception {
        Long managerId = 1L;
        when(projectService.getProjectsByManager(managerId)).thenThrow(new ProjectNotFoundException("No projects found"));

        mockMvc.perform(get("/api/v2/project/getProjects/{managerId}", managerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No projects found under this manager"));

        verify(projectService, times(1)).getProjectsByManager(managerId);
    }

    @Test
    void testDeleteProjectsByManagerForTest() throws Exception {
        Long projectId = 1L;
        when(projectService.deleteProjectsByManager(projectId)).thenReturn("Deleted");

        mockMvc.perform(delete("/api/v2/project/deleteProjectsForTest/{projectId}", projectId))
                .andExpect(status().isOk());
        
        verify(projectService, times(1)).deleteProjectsByManager(projectId);
    }

    @Test
    void testDeleteProjectsByManagerForTest_ProjectNotFoundException() throws Exception {
        Long projectId = 1L;
        doThrow(new ProjectNotFoundException("Project not found")).when(projectService).deleteProjectsByManager(projectId);

        mockMvc.perform(delete("/api/v2/project/deleteProjectsForTest/{projectId}", projectId))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).deleteProjectsByManager(projectId);
    }
}
