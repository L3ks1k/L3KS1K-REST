package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.DTO.User.Response.KeyResponse;
import com.example.l3ks1krestapi.DTO.User.Response.UserResponse;
import com.example.l3ks1krestapi.Exceptions.UserDoesntExistsException;
import com.example.l3ks1krestapi.Model.OnetimePrekey;
import com.example.l3ks1krestapi.Model.User;
import com.example.l3ks1krestapi.Repository.OnetimePrekeyRepository;
import com.example.l3ks1krestapi.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OnetimePrekeyRepository onetimePrekeyRepository;
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

    public KeyResponse getKeys(String userUUID){
        UUID uuid = UUID.fromString(userUUID);
        User user = userRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        List<String> onetimePrekeys = onetimePrekeyRepository.findAllByUser(user).stream()
                .map(OnetimePrekey::getOneTimePrekey)
                .collect(Collectors.toList());
        return KeyResponse.builder()
                .UUID(user.getUuid().toString())
                .identityKey(user.getIdentityKey())
                .signedPrekey(user.getSignedIdentityKey())
                .oneTimePrekeys(onetimePrekeys)
                .build();

    }

}
