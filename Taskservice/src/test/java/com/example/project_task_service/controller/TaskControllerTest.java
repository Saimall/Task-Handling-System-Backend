package com.example.project_task_service.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.project_task_service.dto.TaskRequestDto;
import com.example.project_task_service.dto.TaskResponseDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.exceptions.TaskNotFoundException;
import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    void setup() {
        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTaskTitle("New Task");
        taskRequestDto.setTaskDescription("Task Description");
        
        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setTaskTitle("New Task");
        taskResponseDto.setTaskDescription("Task Description");
    }

    @Test
    void testAddTaskToProject_Success() throws Exception {
        Long projectId = 1L;
        String token = "Bearer test-token";

        Task createdTask = new Task();
        createdTask.setTaskTitle(taskRequestDto.getTaskTitle());
        createdTask.setTaskDescription(taskRequestDto.getTaskDescription());

        when(taskService.addTaskToProject(eq(projectId), any(TaskRequestDto.class))).thenReturn(createdTask);

        mockMvc.perform(post("/api/v2/task/addTask/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAddTaskToProject_ProjectNotFound() throws Exception {
        Long projectId = 1L;
        String token = "Bearer test-token";

        when(taskService.addTaskToProject(eq(projectId), any(TaskRequestDto.class)))
                .thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(post("/api/v2/task/addTask/{projectId}", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, token)
                .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTaskByProjectId_Success() throws Exception {
        Long projectId = 1L;

        when(taskService.getTaskByProjectId(projectId)).thenReturn(List.of(taskResponseDto));

        mockMvc.perform(get("/api/v2/task/getTaskByProjectId/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskTitle").value("New Task"));
    }

    @Test
    void testGetTaskByProjectId_ProjectNotFound() throws Exception {
        Long projectId = 1L;

        when(taskService.getTaskByProjectId(projectId)).thenThrow(new ProjectNotFoundException("Project not found"));

        mockMvc.perform(get("/api/v2/task/getTaskByProjectId/{projectId}", projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTasksByCreatedDate_Success() throws Exception {
        String createdDate = "2023-01-01";

        when(taskService.getTasksByCreatedDate(LocalDate.parse(createdDate))).thenReturn(List.of(taskResponseDto));

        mockMvc.perform(get("/api/v2/task/getTasksByCreatedDate/{createdDate}", createdDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskTitle").value("New Task"));
    }

    @Test
    void testGetTasksByStatus_Success() throws Exception {
        Status status = Status.IN_PROGRESS;

        when(taskService.getTasksByStatus(status)).thenReturn(List.of(taskResponseDto));

        mockMvc.perform(get("/api/v2/task/getTasksByStatus/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].taskTitle").value("New Task"));
    }

    @Test
    void testUpdateTask_Success() throws Exception {
        Long taskId = 1L;

        when(taskService.updateTask(eq(taskId), any(TaskRequestDto.class))).thenReturn(taskResponseDto);

        mockMvc.perform(put("/api/v2/task/updateTasks/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taskTitle").value("New Task"));
    }

    @Test
    void testDeleteTask_Success() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(delete("/api/v2/task/deleteTask/{taskId}", taskId))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(taskId);
    }

    @Test
    void testDeleteTask_TaskNotFound() throws Exception {
        Long taskId = 1L;

        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(taskId);

        mockMvc.perform(delete("/api/v2/task/deleteTask/{taskId}", taskId))
                .andExpect(status().isNotFound());
    }
}
