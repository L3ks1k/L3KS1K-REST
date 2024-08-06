package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.DTO.User.Response.UserResponse;
import com.example.l3ks1krestapi.Exceptions.UserDoesntExistsException;
import com.example.l3ks1krestapi.Model.User;
import com.example.l3ks1krestapi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public UserResponse getMe(String token){
        String username = jwtService.extractUsername(token.substring(7));
        System.out.println(username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserDoesntExistsException("No such user"));
        return UserResponse.builder()
                .UUID(user.getUuid().toString())
                .username(user.getUsername())
                .identityKey(user.getIdentityKey())
                .signedIdentityKey(user.getSignedIdentityKey())
                .build();
    }

    public UserResponse getUser(String userUUID){
        UUID uuid = UUID.fromString(userUUID);
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        return UserResponse.builder()
                .UUID(user.getUuid().toString())
                .username(user.getUsername())
                .identityKey(user.getIdentityKey())
                .signedIdentityKey(user.getSignedIdentityKey())
                .build();
    }
}
