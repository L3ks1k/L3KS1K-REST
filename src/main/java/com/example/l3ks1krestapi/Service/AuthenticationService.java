package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.DTO.Auth.Request.RegistrationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Response.RegistrationResponse;
import com.example.l3ks1krestapi.Exceptions.InsecurePasswordException;
import com.example.l3ks1krestapi.Exceptions.UserExistsException;
import com.example.l3ks1krestapi.Model.Role;
import com.example.l3ks1krestapi.Model.User;
import com.example.l3ks1krestapi.Repository.UserRepository;
import com.example.l3ks1krestapi.Security.PasswordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordHandler passwordHandler = new PasswordHandler();
    public RegistrationResponse register(RegistrationRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UserExistsException("User with provided email, already exists.");
        }
        if (request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Password mismatch.");
        }
        if (passwordHandler.isOnBlackList(request.getPassword())){
            throw new InsecurePasswordException("Password was compromised.");
        }
        if (!(passwordHandler.checkLength(request.getPassword()))){
            throw new InsecurePasswordException("Password's length must be in range <12,64>");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return RegistrationResponse.builder()
                .username(user.getUsername())
                .uuid(user.getUuid().toString())
                .email(user.getEmail())
                .build();
    }
}
