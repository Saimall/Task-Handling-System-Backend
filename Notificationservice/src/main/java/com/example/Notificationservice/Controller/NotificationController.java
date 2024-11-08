package com.example.Notificationservice.Controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Notificationservice.DTO.EmailrequestDto;
import com.example.Notificationservice.EmailService.EmailService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
	
	
	 @Autowired
	    private EmailService emailService;

	    @PostMapping("/sendEmail")
	    public ResponseEntity<String> sendEmailNotification(@RequestBody EmailrequestDto emailRequest) {
	    	
	    	System.out.println(emailRequest.getBody());
	        emailService.sendEmail(emailRequest.getToEmail(), emailRequest.getSubject(), emailRequest.getBody());
	        return ResponseEntity.ok("Email sent successfully");
	    }
	

}
