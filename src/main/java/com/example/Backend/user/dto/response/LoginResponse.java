package com.example.Backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
  private String accessToken;   // 액세스 토큰
  private String refreshToken;  // 리프레시 토큰
}
