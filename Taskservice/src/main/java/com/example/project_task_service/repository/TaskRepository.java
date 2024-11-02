package com.example.project_task_service.repository;

import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
//    List<Task> findByDueDate(LocalDate dueDate);

//    List<Task> findByCreatedAt(LocalDateTime createdAt);
    @Query("SELECT t FROM Task t WHERE FUNCTION('DATE', t.createdAt) = :createdDate")
    List<Task> findByCreatedDate(@Param("createdDate") LocalDate createdDate);

    List<Task> findByStatus(Status status);


    List<Task> findByEmployeeId(Long employeeId);
}
