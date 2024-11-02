package com.example.Securityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.Securityservice.models.AccessGuard;

@Repository
public interface Userrepository extends JpaRepository<AccessGuard, Integer> {

	AccessGuard findByUsername(String username);
	
	boolean existByFamilyid(Integer familyid);
	

	
	
}
