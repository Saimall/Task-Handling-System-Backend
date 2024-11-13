package com.example.user_service.exceptions;

public class EmployeeAlreadyExistException extends RuntimeException{
    public EmployeeAlreadyExistException(String message) {
        super(message);
    }
}
