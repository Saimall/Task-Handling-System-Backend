package com.example.Securityservice.DTOS;

import java.sql.Date;




public class Healthcardpojo extends Layoutpojo {
	
public Healthcardpojo() {
		
	}
	

	private Date issueDate;
	
	
	private Date expireDate;
	private String username; 
	private int policynumber;
	
	public Healthcardpojo(Integer userid, Integer  number2, String entityName2, Date issueDate, Date expireDate, String username, int policynumber) {
		super(number2, entityName2, userid);
		this.issueDate = issueDate;
		this.expireDate = expireDate;
		this.username = username;
		this.policynumber = policynumber;
	}



	
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
	public int getPolicynumber() {
		return policynumber;
	}
	public void setPolicynumber(int policynumber) {
		this.policynumber = policynumber;
	}
	
	@Override
	public String toString() {
		return "HeatlthCard [issueDate=" + issueDate + ", expireDate=" + expireDate + ", username=" + username
				+ ", policynumber=" + policynumber + "]";
	}
	
}
