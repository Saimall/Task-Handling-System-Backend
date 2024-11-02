package com.example.Securityservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AccessGuard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String username;
	private String password;
	private int familyid;
	
	public int getFamilyid() {
		return familyid;
	}



	public void setFamilyid(int familyid) {
		this.familyid = familyid;
	}



	@Override
	public String toString() {
		return "AccessGuard [id=" + id + ", username=" + username + ", password=" + password + ", familyid=" + familyid
				+ "]";
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

}
