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
                    .allowedOrigins("https://task-handling-system-frontend.vercel.app") 
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
                    .allowedHeaders("*")
                    .allowCredentials(true); 
        }
}
