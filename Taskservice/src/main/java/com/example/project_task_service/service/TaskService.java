package com.example.project_task_service.service;

import com.example.project_task_service.dto.EmailRequestDto;
import com.example.project_task_service.dto.EmployeeDto;
import com.example.project_task_service.dto.ManagerDashboardDTo;
import com.example.project_task_service.dto.TaskRequestDto;
import com.example.project_task_service.dto.TaskResponseDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.exceptions.TaskNotFoundException;
import com.example.project_task_service.model.Project;
import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.repository.ProjectRepository;
import com.example.project_task_service.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties.Restclient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
//@EnableScheduling
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	

	public Task addTaskToProject(Long projectId, TaskRequestDto taskDto) throws Exception {
		Project project = projectRepository.findById(projectId).orElseThrow(
				() -> new ProjectNotFoundException("Project not found to assign task with ID: " + projectId));

		Task task = Task.builder().taskTitle(taskDto.getTaskTitle()).taskDescription(taskDto.getTaskDescription())
				.dueDateTime(taskDto.getDueDateTime()).priority(taskDto.getPriority())
				.employeeId(taskDto.getEmployeeId()).status(Status.TODO).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).completedAt(null).project(project).build();

		System.out.println(task.getEmployeeId());
  

		return taskRepository.save(task);
	}

	// getting employee email
	

	public List<TaskResponseDto> getTaskByProjectId(Long projectId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException("Project not found with ID " + projectId));

		List<Task> tasks = project.getTasks();
		if (tasks.isEmpty()) {
			throw new TaskNotFoundException("No tasks found for project with ID " + projectId);
		}

		return tasks.stream()
				.map(task -> TaskResponseDto.builder().taskId(task.getTaskId()).taskTitle(task.getTaskTitle())
						.taskDescription(task.getTaskDescription()).dueDateTime(task.getDueDateTime())
						.priority(task.getPriority()).employeeId(task.getEmployeeId()).status(task.getStatus()).build())
				.toList();
	}



	public List<TaskResponseDto> getTasksByCreatedDate(LocalDate createdDate) {
		List<Task> tasks = taskRepository.findByCreatedDate(createdDate);
		if (tasks.isEmpty()) {
			throw new TaskNotFoundException("No tasks found with created date " + createdDate);
		}

		return tasks.stream()
				.map(task -> TaskResponseDto.builder().taskId(task.getTaskId()).taskTitle(task.getTaskTitle())
						.taskDescription(task.getTaskDescription()).dueDateTime(task.getDueDateTime()) // Make sure this
																										// is handled in
																										// your DTO and
																										// model
						.priority(task.getPriority()).employeeId(task.getEmployeeId()).status(task.getStatus()).build())
				.toList();
	}

	public List<TaskResponseDto> getTasksByStatus(Status status) {
		List<Task> tasks = taskRepository.findByStatus(status);
		if (tasks.isEmpty()) {
			throw new TaskNotFoundException("No tasks found with status " + status);
		}

		return tasks.stream()
				.map(task -> TaskResponseDto.builder().taskId(task.getTaskId()).taskTitle(task.getTaskTitle())
						.taskDescription(task.getTaskDescription()).dueDateTime(task.getDueDateTime())
						.priority(task.getPriority()).employeeId(task.getEmployeeId()).status(task.getStatus()).build())
				.toList();
	}

	public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskDto) {
		Task existingTask = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with ID " + taskId));

		existingTask.setTaskTitle(taskDto.getTaskTitle());
		existingTask.setTaskDescription(taskDto.getTaskDescription());
		existingTask.setDueDateTime(taskDto.getDueDateTime());
		existingTask.setPriority(taskDto.getPriority());
		existingTask.setUpdatedAt(LocalDateTime.now());

		taskRepository.save(existingTask);

		return TaskResponseDto.builder().taskId(existingTask.getTaskId()).taskTitle(existingTask.getTaskTitle())
				.taskDescription(existingTask.getTaskDescription()).dueDateTime(existingTask.getDueDateTime())
				.priority(existingTask.getPriority()).employeeId(existingTask.getEmployeeId())
				.status(existingTask.getStatus()).build();
	}

	public TaskResponseDto updateTaskStatus(Long taskId, Status newStatus) {
		Task existingTask = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with ID " + taskId));

		existingTask.setStatus(newStatus);
		existingTask.setUpdatedAt(LocalDateTime.now());

		taskRepository.save(existingTask);

		return TaskResponseDto.builder().taskId(existingTask.getTaskId()).taskTitle(existingTask.getTaskTitle())
				.taskDescription(existingTask.getTaskDescription()).dueDateTime(existingTask.getDueDateTime())
				.priority(existingTask.getPriority()).employeeId(existingTask.getEmployeeId())
				.status(existingTask.getStatus()).build();
	}

	@Transactional
	public String deleteTask(Long taskId) {
		Task existingTask = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with ID " + taskId));

		Project project = existingTask.getProject();
		if (project != null) {
			project.getTasks().remove(existingTask);
			projectRepository.save(project);
		}
		taskRepository.deleteById(taskId);

		return "Task deleted successfully with ID " + taskId;
	}

	public List<TaskResponseDto> getTasksByEmployeeId(Long employeeId) {
		List<Task> tasks = taskRepository.findByEmployeeId(employeeId);
		if (tasks.isEmpty()) {
			throw new TaskNotFoundException("No tasks found for employee with ID " + employeeId);
		}

		return tasks.stream()
				.map(task -> TaskResponseDto.builder().taskId(task.getTaskId()).taskTitle(task.getTaskTitle())
						.taskDescription(task.getTaskDescription()).dueDateTime(task.getDueDateTime())
						.priority(task.getPriority()).employeeId(task.getEmployeeId()).status(task.getStatus()).build())
				.toList();
	}
	
	 private String getManagerEmail(Long managerId) {
		 
		
		RestClient restClient =  RestClient.create();
		 
	        String url = "http://localhost:9093/api/v1/manager/viewManagerDetails/" + managerId;
	        ManagerDashboardDTo managerDetails = restClient.get()
	                .uri(url)
	                .retrieve()
	                .body(ManagerDashboardDTo.class);
	              

	        return managerDetails != null ? managerDetails.getEmail() : null;
	    }

	public TaskResponseDto submitTaskForReview(Long taskId) {
		Task existingTask = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with ID " + taskId));

		if (existingTask.getStatus() != Status.COMPLETED) {
			existingTask.setStatus(Status.IN_REVIEW);
			existingTask.setUpdatedAt(LocalDateTime.now());
			taskRepository.save(existingTask);
		}
		

		Long managerId = existingTask.getProject().getManagerId();
        String managerEmail = getManagerEmail(managerId);
        
        if (managerEmail != null) {
            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setToEmail(managerEmail);
            emailRequestDto.setSubject("Task Submitted for Review");
            emailRequestDto.setBody("The task titled '" + existingTask.getTaskTitle() +
                    "' has been submitted for review by the employee.");

            try {
            	RestClient restClient =  RestClient.create();
                 restClient.post()
                        .uri("http://localhost:9093/notifications/sendEmail")
                        .body(emailRequestDto)
                        .retrieve()
                        .toBodilessEntity();
                        
                System.out.println("Notification sent Successfully " );
            } catch (Exception e) {
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        }
        
		
		return TaskResponseDto.builder().taskId(existingTask.getTaskId()).taskTitle(existingTask.getTaskTitle())
				.taskDescription(existingTask.getTaskDescription()).dueDateTime(existingTask.getDueDateTime())
				.priority(existingTask.getPriority()).employeeId(existingTask.getEmployeeId())
				.status(existingTask.getStatus()).build();
	}

}
