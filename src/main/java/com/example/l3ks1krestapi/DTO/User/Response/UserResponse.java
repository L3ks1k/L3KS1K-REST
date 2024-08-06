package com.example.l3ks1krestapi.DTO.User.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String UUID;
    private String username;
    private String identityKey;
    private String signedIdentityKey;
}
