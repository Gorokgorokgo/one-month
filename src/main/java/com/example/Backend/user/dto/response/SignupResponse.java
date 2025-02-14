package com.example.Backend.user.dto.response;

import com.example.Backend.auth.dto.response.AuthorityResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SignupResponse {
  private String username;
  private String nickname;
  private List<AuthorityResponse> authorities;

}
