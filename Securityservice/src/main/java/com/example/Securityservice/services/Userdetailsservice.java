package com.example.Securityservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Securityservice.models.AccessGuard;
import com.example.Securityservice.repository.Userrepository;

@Service
public class Userdetailsservice  implements UserDetailsService{
	
	
	@Autowired
	Userrepository userrepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AccessGuard accessGuard = userrepository.findByUsername(username);
		System.out.print(accessGuard);
		if(accessGuard!=null) {			
			return new Userdetailsimplements(accessGuard);
		}
		throw new UsernameNotFoundException("Username not found");
	}

}
