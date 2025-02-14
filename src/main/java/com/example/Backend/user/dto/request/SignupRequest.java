package com.example.Backend.user.dto.request;

import lombok.Getter;

@Getter
public class SignupRequest {
  private String username;
  private String email;
  private String password;

}
