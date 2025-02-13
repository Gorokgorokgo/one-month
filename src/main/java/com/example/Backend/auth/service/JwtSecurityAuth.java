package com.example.Backend.auth.service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import com.example.Backend.auth.entity.RefreshToken;
import com.example.Backend.auth.repository.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Component
public class JwtSecurityAuth {

  @Value("${jwt.secret}")
  private String secretKey;

  private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 30; // 30분
  private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7일

  private SecretKey key;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @PostConstruct
  public void init() {
    System.out.println("Loaded secretKey: " + secretKey);
    try {
      if (secretKey == null || secretKey.isEmpty()) {
        throw new IllegalStateException("JWT secret key가 없음.");
      }
      this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    } catch (Exception e) {
      System.err.println("JWT secret key 초기화 중 오류 발생: " + e.getMessage());
      throw new RuntimeException("JWT 초기화 실패", e);
    }
  }
  // Access Token과 Refresh Token 생성
  public String generateToken(String username, boolean isAccessToken) {
    long expiration = isAccessToken ? ACCESS_TOKEN_EXPIRATION : REFRESH_TOKEN_EXPIRATION;
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  // Refresh Token을 이용해 새로운 Access Token 발급
  public String refreshAccessToken(String refreshToken) {
    if (!validateToken(refreshToken)) {
      throw new JwtException("유효하지 않거나, 만료된 토큰입니다.");
    }

    String username = getUsernameFromToken(refreshToken);
    return generateToken(username, true);  // Access Token을 새로 발급
  }

  // JWT 토큰의 유효성 검사
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      // 토큰 만료 처리
      System.out.println("만료된 토큰 : " + e.getMessage());
    } catch (UnsupportedJwtException e) {
      // 지원되지 않는 토큰 형식 처리
      System.out.println("지원하지 않는 토큰 : " + e.getMessage());
    } catch (JwtException | IllegalArgumentException e) {
      // 잘못된 토큰 처리
      System.out.println("유효하지 않은 토큰 : " + e.getMessage());
    }
    return false;
  }

  // 토큰에서 사용자 이름 추출
  public String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  // 로그아웃 처리 (Refresh Token DB에서 삭제)
  public void logout(String refreshToken) {
    Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);
    tokenOptional.ifPresent(refreshTokenRepository::delete);
  }

  // 인증된 사용자의 Access Token 정보 확인
  public String getAuthenticatedUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication != null) ? authentication.getName() : null;
  }
}