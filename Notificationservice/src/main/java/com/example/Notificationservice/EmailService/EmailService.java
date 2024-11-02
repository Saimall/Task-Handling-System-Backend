package com.example.Notificationservice.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	  @Autowired
	   private JavaMailSender mailSender;

	    public void sendEmail(String toEmail, String subject, String body,String senderEmail) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject(subject);
	        message.setText(body);
	        message.setFrom(senderEmail);
	        mailSender.send(message);
	    }
	
	

}
