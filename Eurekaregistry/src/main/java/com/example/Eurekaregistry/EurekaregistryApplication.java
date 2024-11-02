package com.example.Eurekaregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaregistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaregistryApplication.class, args);
	}

}
