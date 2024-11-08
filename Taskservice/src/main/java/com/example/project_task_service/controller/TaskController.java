package com.example.project_task_service.controller;
import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.project_task_service.dto.EmailRequestDto;
import com.example.project_task_service.dto.EmployeeDashboardDto;
import com.example.project_task_service.dto.EmployeeDto;
import com.example.project_task_service.dto.TaskRequestDto;
import com.example.project_task_service.dto.TaskResponseDto;
import com.example.project_task_service.exceptions.ProjectNotFoundException;
import com.example.project_task_service.exceptions.TaskNotFoundException;
import com.example.project_task_service.model.Status;
import com.example.project_task_service.model.Task;
import com.example.project_task_service.service.TaskService;

@RestController
@RequestMapping("/api/v2/task")
public class TaskController {
	@Autowired
	private TaskService taskService;

	@PostMapping("/addTask/{projectId}")
	public ResponseEntity<Task> addTaskToProject(@PathVariable Long projectId,
			@RequestBody TaskRequestDto taskRequestDto, @RequestHeader("Authorization") String token) {
		try {
			Task createdTask = taskService.addTaskToProject(projectId, taskRequestDto);
			System.out.println("Created task"+createdTask);
			System.out.println("EmployeeID"+createdTask.getEmployeeId());
			String url = "http://localhost:9093/api/v1/employee/viewEmployeeDetails/" + createdTask.getEmployeeId();
			System.out.println(url);
			WebClient webClient = WebClient.create();
			 EmailRequestDto emailRequestDto = new EmailRequestDto();
			webClient.get()
		    .uri(url)
		    .header(HttpHeaders.AUTHORIZATION,token)
		    .retrieve()
		    .bodyToMono(EmployeeDashboardDto.class)
		    .subscribe(
		        dto -> {
		            System.out.println("Employee details retrieved: " + dto);

		           
		            emailRequestDto.setBody("Task Added with " + createdTask.getTaskDescription() + "\n" + "Task Deadline: " + createdTask.getDueDateTime());
		            emailRequestDto.setSubject("Task Added with title " + createdTask.getTaskTitle());
		            emailRequestDto.setToEmail(dto.getEmail());
		            System.out.println("Preparing to send email notification...");
		            
		            
		            String notificationUrl = "http://localhost:9093/notifications/sendEmail";
		            System.out.println("Emaillbody"+emailRequestDto);
		            
		                       webClient.post()
		                           .uri(notificationUrl)
		                           .header(HttpHeaders.AUTHORIZATION,token)
		                           .bodyValue(emailRequestDto)
		                           .retrieve()
		                           .toBodilessEntity()
		                           .subscribe(
		                               response -> System.out.println("Email sent successfully"),
		                               error -> System.err.println("Failed to send email: " + error.getMessage())
		                           );
		            
		        },
		        error -> System.err.println("Failed to retrieve employee details: " + error.getMessage())
		    );
			
			
			
			
			return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
		} catch (ProjectNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/getTaskByProjectId/{projectId}")
	public ResponseEntity<List<TaskResponseDto>> getTaskByProjectId(@PathVariable Long projectId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskByProjectId(projectId));
		} catch (ProjectNotFoundException | TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/getTasksByCreatedDate/{createdDate}")
	public ResponseEntity<List<TaskResponseDto>> getTasksByCreatedDate(@PathVariable String createdDate) {
		try {
			// Parse the date from string
			LocalDate date = LocalDate.parse(createdDate);
			return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasksByCreatedDate(date));
		} catch (DateTimeParseException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Or return a meaningful error message
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/getTasksByStatus/{status}")
	public ResponseEntity<List<TaskResponseDto>> getTasksByStatus(@PathVariable Status status) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasksByStatus(status));
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/updateTasks/{taskId}")
	public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long taskId,
			@RequestBody TaskRequestDto taskRequestDto) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(taskId, taskRequestDto));
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/updateTaskStatus/{taskId}/{status}")
	public ResponseEntity<TaskResponseDto> updateTaskStatus(@PathVariable Long taskId, @PathVariable String status) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(taskService.updateTaskStatus(taskId, Status.valueOf(status)));
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@DeleteMapping("/deleteTask/{taskId}")
	public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteTask(taskId));
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/getTasksByEmployeeId/{employeeId}")
	public ResponseEntity<List<TaskResponseDto>> getTasksByEmployeeId(@PathVariable Long employeeId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasksByEmployeeId(employeeId));
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/submitTaskForReview/{taskId}")
	public ResponseEntity<TaskResponseDto> submitTaskForReview(@PathVariable Long taskId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(taskService.submitTaskForReview(taskId));
			
			
		} catch (TaskNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
