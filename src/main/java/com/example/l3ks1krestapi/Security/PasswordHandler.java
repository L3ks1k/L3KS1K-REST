package com.example.l3ks1krestapi.Security;

import com.example.l3ks1krestapi.Exceptions.InsecurePasswordException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PasswordHandler {
    public boolean checkLength(String password){
        return password.length() >= 12 && password.length() < 64;
    }

    public boolean isOnBlackList(String password){
        try{
            // TODO: Find a better way to get weakpass.txt file
            String root = System.getProperty("user.dir");
            Path path = Paths.get(root + "/src/main/resources/weakpass.txt");
            return Files.lines(path).anyMatch(l -> l.contains(password));
        } catch (IOException e){
            throw new RuntimeException("Unexpected error during password security check.");
        }
    }
}
