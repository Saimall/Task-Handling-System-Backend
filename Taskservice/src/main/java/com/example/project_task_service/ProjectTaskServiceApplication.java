package com.example.project_task_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class ProjectTaskServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectTaskServiceApplication.class, args);
	}
}