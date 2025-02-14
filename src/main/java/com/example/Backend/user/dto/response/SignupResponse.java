package com.example.Backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

@Getter
public class SignupResponse {
  private String email;
  private String password;
}
