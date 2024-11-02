package com.example.Securityservice.DTOS;

import java.sql.Date;


public class Rtopojo extends Layoutpojo {
	
public Rtopojo() {
		
	}

	
	@Override
	public String toString() {
		return "Rtopojo [issueDate=" + issueDate + ", expireDate=" + expireDate + ", username=" + username + "]";
	}

	private Date expireDate;
	private String username; 
	private Date issueDate;
	public Date getIssueDate() {
		return issueDate;
	}


	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}


	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	

	public Rtopojo(int userid, int number2, String entityName2, Date expireDate, Date issueDate, String username) {
		super(number2, entityName2,userid);
		this.expireDate=expireDate;
		this.issueDate=issueDate;
		this.username=username;
		
	}


}
