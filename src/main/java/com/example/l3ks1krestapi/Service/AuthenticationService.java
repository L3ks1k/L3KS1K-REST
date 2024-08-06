package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.DTO.Auth.Request.AuthenticationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Request.ChangePasswordRequest;
import com.example.l3ks1krestapi.DTO.Auth.Request.RegistrationRequest;
import com.example.l3ks1krestapi.DTO.Auth.Response.AuthenticationResponse;
import com.example.l3ks1krestapi.DTO.Auth.Response.ChangePasswordResponse;
import com.example.l3ks1krestapi.DTO.Auth.Response.RegistrationResponse;
import com.example.l3ks1krestapi.DTO.Auth.Response.RevokeResponse;
import com.example.l3ks1krestapi.Exceptions.CompromisedPasswordException;
import com.example.l3ks1krestapi.Exceptions.InvalidPasswordLengthException;
import com.example.l3ks1krestapi.Exceptions.UserExistsException;
import com.example.l3ks1krestapi.Model.RevokedToken;
import com.example.l3ks1krestapi.Model.User;
import com.example.l3ks1krestapi.Repository.RevokedTokenRepository;
import com.example.l3ks1krestapi.Repository.UserRepository;
import com.example.l3ks1krestapi.Security.PasswordHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RevokedTokenRepository revokedTokenRepository;
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
            throw new CompromisedPasswordException("Password was compromised.");
        }
        if (!(passwordHandler.checkLength(password))){
            throw new InvalidPasswordLengthException("Password's length must be in range <12,128>");
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
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Bad credentials.");
        }


        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public RevokeResponse revokeToken(String token) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA3-256");
        byte[] digest = messageDigest.digest(token.substring(7).getBytes());
        String tokenDigest = new BigInteger(1, digest).toString(16);
        revokedTokenRepository.save(RevokedToken.builder().revokedTokenHash(tokenDigest).build());
        return RevokeResponse.builder()
                .message("Token revoked")
                .build();
    }
    public ChangePasswordResponse changePassword(String token, ChangePasswordRequest request){
        String username = jwtService.extractUsername(token);
        var user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordHandler.isOnBlackList(request.getNewPassword())){
            throw new CompromisedPasswordException("Password was compromised.");
        }
        if (!(passwordHandler.checkLength(request.getNewPassword()))){
            throw new InvalidPasswordLengthException("Password's length must be in range <12,128>");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ChangePasswordResponse.builder()
                .message("Password changed")
                .build();
    }

}
