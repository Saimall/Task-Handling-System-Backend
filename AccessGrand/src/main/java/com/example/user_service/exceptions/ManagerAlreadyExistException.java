package com.example.user_service.exceptions;

public class ManagerAlreadyExistException extends RuntimeException{
    public ManagerAlreadyExistException(String message) {
        super(message);
    }
}
