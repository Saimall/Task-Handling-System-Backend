package com.example.user_service.controller;

import com.example.user_service.dto.AuthenticationResponse;
import com.example.user_service.exceptions.EmployeeNotFoundException;
import com.example.user_service.model.Usermain;
import com.example.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody Usermain user) {

        try {
            return ResponseEntity.ok(service.verify(user));
        }
        catch (EmployeeNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


}