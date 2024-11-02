package com.example.Securityservice.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.Securityservice.services.Userdetailsservice;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private Userdetailsservice userdetailsservice;
	
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(customizer->customizer.disable()).authorizeHttpRequests(request->request.anyRequest().permitAll()).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults()).oauth2Login(Customizer.withDefaults());
		
		return http.build();
	}

	
	@Bean
	AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		daoAuthenticationProvider.setUserDetailsService(userdetailsservice);
		
		return daoAuthenticationProvider;
		
	}
	
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		
		return configuration.getAuthenticationManager();
	}
	
	
	
}
