package com.example.Securityservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Securityservice.models.AccessGuard;
import com.example.Securityservice.repository.Userrepository;

@Service
public class Userservice {
	
	@Autowired
	private Userrepository userrepository;
	
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	

	public AccessGuard adduser(AccessGuard accessGuard) {
		if(userrepository.existsByFamilyid(accessGuard.getFamilyid())) {
			throw new Exception("FamilyID already present kindly choose differnt FamilyID");
		}
		else {
		accessGuard.setPassword(passwordEncoder.encode(accessGuard.getPassword()));
		}
		
		return userrepository.save(accessGuard);
	}



	public List<AccessGuard> getusers() {
		
		return userrepository.findAll();
	}



	public List<AccessGuard> finallusers() {
		return userrepository.findAll();
	}



	public String generateToken(String username) {
		return JWTservice.generatetoken(username);
	}



	public boolean validatetoken(String token) {
		
		return JWTservice.validateToken(token);
	}



	

}
