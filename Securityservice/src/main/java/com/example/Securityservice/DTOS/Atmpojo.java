package com.example.Securityservice.DTOS;

import java.sql.Date;







public class Atmpojo  extends Layoutpojo{
	
	private Date issueDate;
	private Date expireDate;
	private String username;
	private int cvv;
	
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

	public int getCvv() {
		return cvv;
	}

	public void setCvv(int cvv) {
		this.cvv = cvv;
	}

	public Atmpojo() {
		
	}
	

	public Atmpojo(int userid, int number2, String entityName2, Date issueDate, Date expireDate, int cvv, String username) {
		super(number2, entityName2,userid);
		this.issueDate = issueDate;
		this.expireDate=expireDate;
		this.cvv=cvv;
		this.username=username;	
	}
	
	
	@Override
	public String toString() {
		return "Atmcards [issueDate=" + issueDate + ", expireDate=" + expireDate + ", username=" + username + ", CCV="
				+ cvv + "]";
	}


}
