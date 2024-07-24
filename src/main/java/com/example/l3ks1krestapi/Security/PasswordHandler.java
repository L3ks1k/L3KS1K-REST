package com.example.l3ks1krestapi.Security;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PasswordHandler {
    public boolean checkLength(String password){
        return password.length() >= 12 && password.length() < 128;
    }

    public boolean isOnBlackList(String password){
        try{
            URI uri = ClassLoader.getSystemResource("weakpass.txt").toURI();
            String pathStr = Paths.get(uri).toString();
            Path path = Paths.get(pathStr);
            return Files.lines(path).anyMatch(l -> l.contains(password));
        } catch (IOException e){
            throw new RuntimeException("Unexpected error during password security check.");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public String removeRedundantSpaces(String password){
       return password.trim().replaceAll("\\s+"," ");
    }
}
