package com.example.l3ks1krestapi.Exceptions;

public class UserDoesntExistsException extends RuntimeException{
    public UserDoesntExistsException(String message){
        super(message);
    }
}
