package com.example.Securityservice.controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Securityservice.DTOS.Atmpojo;
import com.example.Securityservice.DTOS.Filespojo;
import com.example.Securityservice.DTOS.Foodcardpojo;
import com.example.Securityservice.DTOS.Healthcardpojo;
import com.example.Securityservice.DTOS.Layoutpojo;
import com.example.Securityservice.DTOS.Rtopojo;
import com.example.Securityservice.models.AccessGuard;
import com.example.Securityservice.services.Userdetailsservice;
import com.example.Securityservice.services.Userservice;

@RestController
@RequestMapping("/user")
public class Usercontroller {
	
	
	@Autowired
	private Userservice userservice;
	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@Autowired
	private FiegnclientInterface fiegnclientInterface;
	
	

	
	
	
	@PostMapping("/add")
	public AccessGuard adduser(@RequestBody AccessGuard accessGuard){
		return userservice.adduser(accessGuard);
		
	}
	
	

   @GetMapping("/get")
   public List<AccessGuard> getusers(){
	   return userservice.finallusers();
   }
   
   
   @PostMapping("/login")
   public String loginuser(@RequestBody AccessGuard accessGuard) {
	   System.out.print("Username : "+accessGuard);
	   
	   Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accessGuard.getUsername(), accessGuard.getPassword()));
	   
	   if(authentication.isAuthenticated()) {
		   return userservice.generateToken(accessGuard.getUsername());
	   }
	   return null;
	   
   }
   
   
   @GetMapping("/validate")
   public boolean loginuser(@RequestParam String token) {
	   
	   return userservice.validatetoken(token);
	  
	   
   }
   
   
   
   @GetMapping("/getallcard/{familyid}")
   public ResponseEntity<?>getallcards(@PathVariable int familyid){
	   
	   System.out.println("Hello all cards");
	   HashMap<Integer, List<?>> allresults = new HashMap<>();
    List<Object> allcardsList = new ArrayList<>();
     List<Atmpojo>atmcards = fiegnclientInterface.getatmcard(familyid);
     List<Filespojo>files = fiegnclientInterface.getallfiles(familyid);
     System.out.print("Files:"+files);
     List<Foodcardpojo>foods = fiegnclientInterface.getallfoodcards(familyid);
     List<Healthcardpojo>healthcards=fiegnclientInterface.getallhealthcards(familyid);
    List<Rtopojo>rtopojos = fiegnclientInterface.getalltropojos(familyid);
    allcardsList.add(healthcards);
    allcardsList.add(rtopojos);
    allcardsList.add(foods);
    allcardsList.add(files);
    allcardsList.add(atmcards);
	
    allresults.put(familyid, allcardsList);
	
     return new ResponseEntity<Map>(allresults,HttpStatus.OK);
     
	
	   
   }
   
  
   
 

   
   
   

}
