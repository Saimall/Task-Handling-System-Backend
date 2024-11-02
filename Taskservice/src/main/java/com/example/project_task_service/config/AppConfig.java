package com.example.project_task_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	   @Bean
	    public RestTemplate restTemplate() {
	        return new RestTemplate();
	    }

}
