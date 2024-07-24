package com.example.l3ks1krestapi.Controller;

import com.example.l3ks1krestapi.DTO.Auth.Request.AuthenticationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Request.RegistrationRequest;
import com.example.l3ks1krestapi.DTO.ErrorCodes;
import com.example.l3ks1krestapi.DTO.Message;
import com.example.l3ks1krestapi.Exceptions.CompromisedPasswordException;
import com.example.l3ks1krestapi.Exceptions.InvalidPasswordLengthException;
import com.example.l3ks1krestapi.Exceptions.UserExistsException;
import com.example.l3ks1krestapi.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){
        try{
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (CompromisedPasswordException | InvalidPasswordLengthException | UserExistsException e){
            return ResponseEntity.ok(e.getMessage());
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request){
        try{
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (BadCredentialsException e){
            return ResponseEntity.ok(Message.builder().message(ErrorCodes.E1000.toString()).errorCode(ErrorCodes.E1000.name()).build());
        }
    }
    @PostMapping("/revoke-token")
    public ResponseEntity<?> revoke(@RequestHeader("Authorization") String token){
        try {
            return ResponseEntity.ok(authenticationService.revokeToken(token));
        } catch (NoSuchAlgorithmException e) {
            return ResponseEntity.ok(Message.builder().message("E1001: Cryptographic failure").errorCode("E1001").build());
        }
    }
    @GetMapping("/test")
    public String test(){
        return "Test";
    }
}
