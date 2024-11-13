package com.example.project_task_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.project_task_service.dto.TaskRequestDto;
import com.example.project_task_service.dto.TaskResponseDto;
import com.example.project_task_service.exceptions.TaskNotFoundException;
import com.example.project_task_service.model.Priority;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.repository.ProjectRepository;
import com.example.project_task_service.repository.TaskRepository;
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;

    private Project project;
    private TaskRequestDto taskRequestDto;
    private Task task;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setProjectId(1L);

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTaskTitle("Test Task");
        taskRequestDto.setTaskDescription("Test Task Description");
        taskRequestDto.setDueDateTime(LocalDateTime.now());
        taskRequestDto.setPriority(Priority.HIGH);
        taskRequestDto.setEmployeeId(1L);

        task = Task.builder()
                .taskId(1L)
                .taskTitle("Test Task")
                .taskDescription("Test Task Description")
                .dueDateTime(LocalDateTime.now())
                .priority(Priority.HIGH)
                .employeeId(1L)
                .status(Status.TODO)
                .project(project)
                .build();
    }

    @Test
    void addTaskToProject_ShouldReturnTask() throws Exception {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.addTaskToProject(1L, taskRequestDto);

        assertNotNull(result);
        assertEquals("Test Task", result.getTaskTitle());
        assertEquals(Status.TODO, result.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void addTaskToProject_ShouldThrowException_WhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> taskService.addTaskToProject(1L, taskRequestDto));
    }

//    @Test
//    void getTaskByProjectId_ShouldReturnTaskResponseDto() {
//        // Arrange
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(project.getTasks().thenReturn(List.of(task));
//
//        // Act
//        List<TaskResponseDto> taskResponseDtos = taskService.getTaskByProjectId(1L);
//
//        // Assert
//        assertNotNull(taskResponseDtos);
//        assertEquals(1, taskResponseDtos.size());
//        assertEquals("Test Task", taskResponseDtos.get(0).getTaskTitle());
//    }

//    @Test
//    void getTaskByProjectId_ShouldThrowException_WhenNoTasksFound() {
//        // Arrange
//        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
//        when(taskRepository.findByProject(project)).thenReturn(List.of(task));
//
//        // Act & Assert
//        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskByProjectId(1L));
//    }

    @Test
    void updateTaskStatus_ShouldUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponseDto result = taskService.updateTaskStatus(1L, Status.COMPLETED, "token");

        assertEquals(Status.COMPLETED, task.getStatus());
        assertEquals("Test Task", result.getTaskTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

//    @Test
//    void deleteTask_ShouldDeleteTask() {
//        // Arrange
//        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
//
//        // Act
//        taskService.deleteTask(1L);
//
//        // Assert
//        verify(taskRepository, times(1)).deleteById(1L);
//    }
}
