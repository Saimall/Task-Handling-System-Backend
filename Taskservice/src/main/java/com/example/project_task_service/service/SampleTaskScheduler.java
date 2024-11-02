package com.example.project_task_service.service;

import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class SampleTaskScheduler {

    @Autowired
    private TaskRepository taskRepository;

//    @Scheduled(cron = "0 * * * * ?") // Runs daily at midnight
//    public void markOverdueTasks() {
//        List<Task> tasks = taskRepository.findAll(); // Fetch all tasks, or apply necessary filters
//        LocalDateTime now = LocalDateTime.now();
//        System.out.println("Checking if scheduler is working");
//        for (Task task : tasks) {
//            System.out.println("Loop working");
//            if (task.getDueDateTime().isBefore(now) && task.getStatus() != Status.COMPLETED && task.getStatus() != Status.IN_REVIEW) {
//                System.out.println("If working");
//                task.setStatus(Status.OVERDUE);
//                task.setUpdatedAt(now);
//                taskRepository.save(task);
//            }
//        }
//    }

    @Scheduled(cron = "0 * * * * ?") // Runs every minute
    public void markOverdueTasks() {
        List<Task> tasks = taskRepository.findAll(); // Fetch all tasks, or apply necessary filters
        LocalDateTime now = LocalDateTime.now();

        for (Task task : tasks) {
            // Use Optional to handle potential null values for dueDateTime
            Optional<LocalDateTime> dueDateTimeOpt = Optional.ofNullable(task.getDueDateTime());
            Optional<Status> statusOpt = Optional.ofNullable(task.getStatus());

            // Check if dueDateTime is present and not before the current time
            if (dueDateTimeOpt.isPresent() && dueDateTimeOpt.get().isBefore(now)) {
                // Use Optional to check status and update task if necessary
                if (statusOpt.isPresent() &&
                        statusOpt.get() != Status.COMPLETED &&
                        statusOpt.get() != Status.IN_REVIEW) {

                    // Set status to OVERDUE, ensuring status is not null
                    task.setStatus(Status.OVERDUE);
                    task.setUpdatedAt(now); // Update the timestamp

                    taskRepository.save(task); // Save the updated task
                } else {
                    System.out.println("Task status is null or in review/completed");
                }
            } else {
                System.out.println("Task's due date is null or not overdue");
            }
        }
    }
}
