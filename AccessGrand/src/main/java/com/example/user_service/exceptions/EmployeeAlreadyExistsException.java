package com.example.user_service.exceptions;

public class EmployeeAlreadyExistsException 
	 extends RuntimeException {
		    public EmployeeAlreadyExistsException(String message) {
		        super(message);
		    }
}


