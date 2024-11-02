package com.example.Securityservice.controllers;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Securityservice.DTOS.Atmpojo;
import com.example.Securityservice.DTOS.Filespojo;
import com.example.Securityservice.DTOS.Foodcardpojo;
import com.example.Securityservice.DTOS.Healthcardpojo;
import com.example.Securityservice.DTOS.Rtopojo;



//change to 9060 will work direct to service
@FeignClient(value = "owner" , url= "http://localhost:9096")
public interface FiegnclientInterface {

	
	@GetMapping("/atm/get/{id}")
	public List <Atmpojo> getatmcard(@PathVariable int id);
	
	
	@GetMapping("/files/get/{id}")
	public List<Filespojo> getallfiles(@PathVariable int id);
	
	@GetMapping("/foodcard/get/{id}")
	public List<Foodcardpojo>getallfoodcards(@PathVariable int id);
	
	@GetMapping("/healthcard/get/{id}")
	public List<Healthcardpojo>getallhealthcards(@PathVariable int id);

    
	@GetMapping("/rto/get/{id}")
	public List<Rtopojo> getalltropojos(@PathVariable int id);
	
}
