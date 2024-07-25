package com.example.l3ks1krestapi.DTO;

public enum ErrorCodes {
    /**
     * E1XXX - Authentication and Authorization Errors
     */
    E1000("Invalid credentials"),
    E1001( "Password compromised"),
    E1002("Invalid password length"),
    E1003("User already exists."),
    E1004( "Token can't be revoked");

    private final String value;
    ErrorCodes(String value){
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
