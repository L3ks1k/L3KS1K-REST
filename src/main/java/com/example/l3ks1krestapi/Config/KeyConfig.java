package com.example.l3ks1krestapi.Config;

import com.example.l3ks1krestapi.Security.KeyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
public class KeyConfig {
    @Value("${private.key.filename}")
    private String privateKeyFilename;
    @Value("${public.key.filename}")
    private String publicKeyFilename;
    @Bean
    public PublicKey publicKey() throws Exception{
        return KeyManager.loadPublicKey(publicKeyFilename);
    }
    @Bean
    public PrivateKey privateKey() throws Exception{
        return KeyManager.loadPrivateKey(privateKeyFilename);
    }

}
