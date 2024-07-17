package com.example.l3ks1krestapi.Controller;

import com.example.l3ks1krestapi.DTO.Auth.Request.AuthenticationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Request.RegistrationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Response.RegistrationResponse;
import com.example.l3ks1krestapi.DTO.Message;
import com.example.l3ks1krestapi.Exceptions.UserExistsException;
import com.example.l3ks1krestapi.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request){
        try{
            return ResponseEntity.ok(authenticationService.register(request));
        } catch (RuntimeException e){
            return ResponseEntity.ok(Message.builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Test");
    }
}
