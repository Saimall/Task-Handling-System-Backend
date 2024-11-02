package com.example.Securityservice.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.Securityservice.models.AccessGuard;

public class Userdetailsimplements implements UserDetails{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AccessGuard accessGuard;
	

	public Userdetailsimplements(AccessGuard accessGuard2) {
		
		this.accessGuard=accessGuard2;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return accessGuard.getPassword();
	}

	@Override
	public String getUsername() {
		return accessGuard.getUsername();
	}

}
