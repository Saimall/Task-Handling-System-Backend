package com.example.Notificationservice.DTO;

public class EmailrequestDto {
	
	private String toEmail;
	private String subject;
    private String body;
	private String fromEmail; 
	
	
	
	public String getFromEmail() {
		return fromEmail;
	}


	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}


	@Override
	public String toString() {
		return "EmailrequestDto [toEmail=" + toEmail + ", subject=" + subject + ", body=" + body + "]";
	}
	
	
	public EmailrequestDto(String toEmail, String subject, String body,String fromEmail) {
		super();
		this.toEmail = toEmail;
		this.subject = subject;
		this.body = body;
		this.fromEmail=fromEmail;
	}
	
	
	
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
