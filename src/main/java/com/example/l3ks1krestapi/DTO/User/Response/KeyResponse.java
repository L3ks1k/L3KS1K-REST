package com.example.l3ks1krestapi.DTO.User.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyResponse {
    private String UUID;
    private String identityKey;
    private String signedPrekey;
    private List<String> oneTimePrekeys;
}
