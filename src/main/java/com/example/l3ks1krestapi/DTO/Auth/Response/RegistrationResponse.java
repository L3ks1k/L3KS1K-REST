package com.example.l3ks1krestapi.DTO.Auth.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class RegistrationResponse {
    private String uuid;
    private String email;
    private String username;
}
