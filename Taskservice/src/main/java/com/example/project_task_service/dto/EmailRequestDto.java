package com.example.project_task_service.dto;

public class EmailRequestDto {
	
	
public EmailRequestDto() {
	
}
	
	private String toEmail;
	private String subject;
    private String body;

	
	
    @Override
	public String toString() {
		return "EmailRequestDto [toEmail=" + toEmail + ", subject=" + subject + ", body=" + body + "]";
	}

	
	 public EmailRequestDto(String toEmail, String subject, String body) {
		super();
		this.toEmail = toEmail;
		this.subject = subject;
		this.body = body;
	}

	

	    

	    // Getters and Setters
	    public String getToEmail() {
	        return toEmail;
	    }

	    public void setToEmail(String toEmail) {
	        this.toEmail = toEmail;
	    }

	    public String getSubject() {
	        return subject;
	    }

	    public void setSubject(String subject) {
	        this.subject = subject;
	    }

	    public String getBody() {
	        return body;
	    }

	    public void setBody(String body) {
	        this.body = body;
	    }
	
	
}
