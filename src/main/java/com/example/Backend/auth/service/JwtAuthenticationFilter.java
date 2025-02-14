package com.example.Backend.auth.service;

import com.example.Backend.auth.entity.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtSecurityAuth jwtSecurityAuth; // JwtSecurityAuth는 JWT 토큰을 다루는 클래스

  public JwtAuthenticationFilter(JwtSecurityAuth jwtSecurityAuth) {
    this.jwtSecurityAuth = jwtSecurityAuth;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 1. HTTP 요청에서 JWT 토큰 추출
    String token = extractToken(request);

    // 2. 토큰이 존재하면 유효성 검사
    if (token != null && jwtSecurityAuth.validateToken(token)) {

      // 3. 토큰에서 사용자 이름을 추출
      String username = jwtSecurityAuth.getUsernameFromToken(token);

      // 4. 이미 인증된 사용자인지 확인
      if (SecurityContextHolder.getContext().getAuthentication() == null) {

        // 5. 인증된 사용자인 경우, Authentication 객체를 생성하고 SecurityContext에 저장
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(username);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    // 6. 필터 체인의 다음 필터로 요청을 전달
    filterChain.doFilter(request, response);
  }

  // JWT 토큰을 HTTP 요청에서 추출하는 메서드
  private String extractToken(HttpServletRequest request) {
    // 헤더에서 "Authorization"을 찾아서 "Bearer " 뒤의 토큰 부분을 반환
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // "Bearer " 부분을 제외한 나머지 토큰
    }
    return null;
  }
}