package com.example.Securityservice.DTOS;



public class Filespojo extends Layoutpojo{
public Filespojo() {
		
	}
	
	private String description;
	
	private String name;
	
	public Filespojo(int userid, int number, String entityName, String description, String name) {
        super(number, entityName,userid);
        this.description = description;
        this.name = name;
    }
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}

