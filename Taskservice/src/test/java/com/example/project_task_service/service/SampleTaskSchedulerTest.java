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
        // Prepare mock behavior
        when(taskRepository.findAll()).thenReturn(Arrays.asList(overdueTask, nonOverdueTask, completedTask, inReviewTask));

        // Call the method to be tested
        sampleTaskScheduler.markOverdueTasks();

        // Verify if the overdue task was updated to 'OVERDUE' status
        verify(taskRepository, times(1)).save(overdueTask);
        assertEquals(Status.OVERDUE, overdueTask.getStatus());
        
        // Verify that non-overdue tasks are not updated
        verify(taskRepository, times(0)).save(nonOverdueTask);
        
        // Verify that completed and in-review tasks are not updated
        verify(taskRepository, times(0)).save(completedTask);
        verify(taskRepository, times(0)).save(inReviewTask);
    }

    @Test
    void testMarkOverdueTasks_NoOverdueTasks() {
        // Prepare mock behavior where all tasks are not overdue
        when(taskRepository.findAll()).thenReturn(Arrays.asList(nonOverdueTask, completedTask, inReviewTask));

        // Call the method to be tested
        sampleTaskScheduler.markOverdueTasks();

        // Verify that no task was updated, since none are overdue
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testMarkOverdueTasks_OverdueTasksWithoutStatusChange() {
        // Task with overdue date and status completed or in review should not be updated
        when(taskRepository.findAll()).thenReturn(Arrays.asList(overdueTask, completedTask, inReviewTask));

        // Call the method to be tested
        sampleTaskScheduler.markOverdueTasks();

        // Verify that completed and in-review tasks are not updated
        verify(taskRepository, times(0)).save(completedTask);
        verify(taskRepository, times(0)).save(inReviewTask);

        // Overdue task should have been updated to 'OVERDUE'
        verify(taskRepository, times(1)).save(overdueTask);
        assertEquals(Status.OVERDUE, overdueTask.getStatus());
    }

    @Test
    void testMarkOverdueTasks_UpdateOnlyValidTasks() {
        // Set up mock data for overdue tasks with various statuses
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

        // Call the method to be tested
        sampleTaskScheduler.markOverdueTasks();

        // Verify that only the valid overdue task gets updated
        verify(taskRepository, times(1)).save(task1);  // Should be updated
        verify(taskRepository, times(0)).save(task2);  // Should not be updated
    }
}
