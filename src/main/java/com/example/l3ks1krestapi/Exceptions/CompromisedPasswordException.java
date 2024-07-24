package com.example.l3ks1krestapi.Exceptions;

public class CompromisedPasswordException extends RuntimeException{
    public CompromisedPasswordException(String message){
        super(message);
    }
}
