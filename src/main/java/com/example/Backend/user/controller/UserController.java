package com.example.Backend.user.controller;

import com.example.Backend.user.dto.request.LoginRequest;
import com.example.Backend.user.dto.request.SignupRequest;
import com.example.Backend.user.dto.response.LoginResponse;
import com.example.Backend.user.dto.response.SignupResponse;
import com.example.Backend.user.entity.User;
import com.example.Backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
    SignupResponse res = userService.signup(signupRequest);
    return ResponseEntity.ok(res);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
    LoginResponse res = userService.login(loginRequest);

    return ResponseEntity.ok(res);
  }
}

