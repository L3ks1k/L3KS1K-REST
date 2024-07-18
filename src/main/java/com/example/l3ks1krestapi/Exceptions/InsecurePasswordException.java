package com.example.l3ks1krestapi.Exceptions;

public class InsecurePasswordException extends RuntimeException{
    public InsecurePasswordException(String message){
        super(message);
    }
}
