package com.example.project_task_service.service;
import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.repository.TaskRepository;
import com.example.project_task_service.service.SampleTaskScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SampleTaskSchedulerTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SampleTaskScheduler sampleTaskScheduler;

    private Task overdueTask;
    private Task nonOverdueTask;
    private Task completedTask;
    private Task inReviewTask;

    @BeforeEach
    public void setUp() {
        overdueTask = Task.builder()
                .taskId(1L)
                .dueDateTime(LocalDateTime.now().minusDays(1))
                .status(Status.OVERDUE)
                .build();

        nonOverdueTask = Task.builder()
                .taskId(2L)
                .dueDateTime(LocalDateTime.now().plusDays(1))
                .status(Status.OVERDUE)
                .build();

        completedTask = Task.builder()
                .taskId(3L)
                .dueDateTime(LocalDateTime.now().minusDays(1))
                .status(Status.COMPLETED)
                .build();

        inReviewTask = Task.builder()
                .taskId(4L)
                .dueDateTime(LocalDateTime.now().minusDays(1))
                .status(Status.IN_REVIEW)
                .build();
    }

    @Test
    void testMarkOverdueTasks_OverdueTasksAreUpdated() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(overdueTask, nonOverdueTask, completedTask, inReviewTask));

        sampleTaskScheduler.markOverdueTasks();

        verify(taskRepository, times(1)).save(overdueTask);
        assertEquals(Status.OVERDUE, overdueTask.getStatus());
        
        verify(taskRepository, times(0)).save(nonOverdueTask);
        
        verify(taskRepository, times(0)).save(completedTask);
        verify(taskRepository, times(0)).save(inReviewTask);
    }

    @Test
    void testMarkOverdueTasks_NoOverdueTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(nonOverdueTask, completedTask, inReviewTask));

        sampleTaskScheduler.markOverdueTasks();

        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testMarkOverdueTasks_OverdueTasksWithoutStatusChange() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(overdueTask, completedTask, inReviewTask));

        sampleTaskScheduler.markOverdueTasks();

        verify(taskRepository, times(0)).save(completedTask);
        verify(taskRepository, times(0)).save(inReviewTask);

        verify(taskRepository, times(1)).save(overdueTask);
        assertEquals(Status.OVERDUE, overdueTask.getStatus());
    }

    @Test
    void testMarkOverdueTasks_UpdateOnlyValidTasks() {
        Task task1 = Task.builder()
                .taskId(1L)
                .dueDateTime(LocalDateTime.now().minusDays(1))
                .status(Status.OVERDUE)
                .build();
        Task task2 = Task.builder()
                .taskId(2L)
                .dueDateTime(LocalDateTime.now().minusDays(1))
                .status(Status.IN_REVIEW)
                .build();

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        sampleTaskScheduler.markOverdueTasks();

        verify(taskRepository, times(1)).save(task1);  
        verify(taskRepository, times(0)).save(task2);  
    }
}
