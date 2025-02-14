package com.example.Backend.user.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
  private String username;
  private String password;
}
