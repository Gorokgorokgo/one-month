package com.example.Backend.user.controller;

import com.example.Backend.user.entity.User;
import com.example.Backend.user.repository.UserRepository;
import com.example.Backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;

  @PostMapping("/signup")
  public User signup(@RequestBody @Valid User user) {
    return userService.signup(user);
  }
}
