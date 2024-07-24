package com.example.l3ks1krestapi.Service;

import com.example.l3ks1krestapi.Exceptions.InvalidTokenException;
import com.example.l3ks1krestapi.Repository.RevokedTokenRepository;
import com.example.l3ks1krestapi.Security.KeyManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final RevokedTokenRepository revokedTokenRepository;
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
    }
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !(isExpired(token)) && !(isRevoked(token));
    }

    private Date extractExpirationDate(String token){
        return extractClaims(token, Claims::getExpiration);
    }

    private boolean isExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }
    public String generateToken(
            Map<String, Objects> extraClaims,
            UserDetails userDetails
    ){
        try {
            return Jwts.builder()
                    .setHeaderParam("typ","JWT")
                    .setClaims(extraClaims)
                    .claim("jti", SecureRandom.getInstanceStrong().nextLong())
                    .setSubject(userDetails.getUsername())
                    .setIssuer("L3KS1K")
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 60 * 180)) // 180 days
                    .signWith(privateKey, SignatureAlgorithm.ES512)
                    .compact();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isRevoked(String token){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e){
            throw new SecurityException("Error during instantiating SHA3-256");
        }
        byte[] hash = digest.digest(token.getBytes());
        String tokenDigest = new BigInteger(1, hash).toString(16);
        return revokedTokenRepository.findByRevokedTokenHash(tokenDigest).isPresent();
    }


}
