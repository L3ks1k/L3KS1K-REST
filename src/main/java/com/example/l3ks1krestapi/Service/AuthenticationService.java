package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.DTO.Auth.Request.AuthenticationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Request.RegistrationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Response.AuthenticationResponse;
import com.example.l3ks1krestapi.DTO.Auth.Response.RegistrationResponse;
import com.example.l3ks1krestapi.Exceptions.InsecurePasswordException;
import com.example.l3ks1krestapi.Exceptions.UserExistsException;
import com.example.l3ks1krestapi.Model.User;
import com.example.l3ks1krestapi.Repository.UserRepository;
import com.example.l3ks1krestapi.Security.PasswordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordHandler passwordHandler = new PasswordHandler();
    public RegistrationResponse register(RegistrationRequest request){
        String password = passwordHandler.removeRedundantSpaces(request.getPassword());
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserExistsException("User with provided email, already exists.");
        }
        if (userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserExistsException("User with provided username, already exists.");
        }
        if (passwordHandler.isOnBlackList(password)){
            throw new InsecurePasswordException("Password was compromised.");
        }
        if (!(passwordHandler.checkLength(password))){
            throw new InsecurePasswordException("Password's length must be in range <12,128>");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return RegistrationResponse.builder()
                .username(user.getUsername())
                .uuid(user.getUuid().toString())
                .email(user.getEmail())
                .build();
    }
    public AuthenticationResponse login(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
