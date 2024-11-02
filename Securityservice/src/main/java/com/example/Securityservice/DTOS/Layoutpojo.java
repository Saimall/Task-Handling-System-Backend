package com.example.Securityservice.DTOS;

public class Layoutpojo {
private int userid;
	

	private int number;
	
	private String entityname;
	
	public Layoutpojo(int number2, String entityName2,int userid) {
		this.number=number2;
		this.entityname = entityName2;
		this.userid=userid;
		// TODO Auto-generated constructor stub
	}
	
	public Layoutpojo() {
		// TODO Auto-generated constructor stub
	}

	public int getUserid() {
		return userid;
	}


	public void setUserid(int userid) {
		this.userid = userid;
	}

	
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getEntityname() {
		return entityname;
	}

	public void setEntityname(String entityname) {
		this.entityname = entityname;
	}
	
	@Override
	public String toString() {
		return "Layout [number=" + number + ", entityname=" + entityname + "]";
	}

	

}
