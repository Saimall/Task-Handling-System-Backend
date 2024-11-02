package com.example.Securityservice.controllers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Securityservice.DTOS.Atmpojo;





@RestController
@RequestMapping("/client")
public class Clientcontroller {
	
	@Autowired
   private FiegnclientInterface fiegnclient;
	
	
   @GetMapping("/home")
    public String greet() {
	 return "Hello world";

   }
   
   @GetMapping("/getatmcard/{id}")
   public List<Atmpojo> getAtmpojo(@PathVariable int id) {
	   System.out.println("hello pojo");
	   return fiegnclient.getatmcard(id);
   }
	 

}
