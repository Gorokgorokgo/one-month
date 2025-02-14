package com.example.Backend.auth.repository;


import com.example.Backend.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

  Optional<RefreshToken> findByToken(String token);

  void deleteByToken(String token);
}
