package com.example.l3ks1krestapi.Exceptions;

public class InvalidPasswordLengthException extends RuntimeException{
    public InvalidPasswordLengthException(String message) {
        super(message);
    }
}
