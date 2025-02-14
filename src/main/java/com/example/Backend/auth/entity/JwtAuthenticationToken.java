package com.example.Backend.auth.entity;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  private final String principal;

  public JwtAuthenticationToken(String principal) {
    super(AuthorityUtils.NO_AUTHORITIES); // 권한이 없는 상태로 초기화
    this.principal = principal;
    setAuthenticated(false); // 인증되지 않은 상태로 설정
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return this.principal; // 인증된 사용자 정보
  }
}
