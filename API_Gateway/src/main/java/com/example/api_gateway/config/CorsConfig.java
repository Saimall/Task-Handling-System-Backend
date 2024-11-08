package com.example.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {


        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**") 
                    .allowedOrigins("http://localhost:4200") // Allow requests from this origin
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP methods
                    .allowedHeaders("*") // Allow any header
                    .allowCredentials(true); // Allow credentials (cookies, authorization headers)
        }
}
