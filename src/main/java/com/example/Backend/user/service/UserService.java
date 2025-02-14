package com.example.Backend.user.service;

import com.example.Backend.user.entity.User;
import com.example.Backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 유저 회원가입
  @Transactional
  public User signup(User user) {

    String encodedPassword = passwordEncoder.encode(user.getPassword());

    // 유저(회원) 생성
    User newUser = new User(user.getUsername(), user.getEmail(), encodedPassword);

    return userRepository.save(newUser); //회원 저장
  }
}
