package com.example.l3ks1krestapi.Repository;

import com.example.l3ks1krestapi.Model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Integer> {
    Optional<RevokedToken> findByRevokedTokenHash(String hash);
}
