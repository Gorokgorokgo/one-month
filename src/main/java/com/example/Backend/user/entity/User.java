package com.example.Backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;  //

  @Column(nullable = false, unique = true)
  private String username;  // 사용자 이름

  @Column(nullable = false, unique = true)
  private String email;  // 사용자 이메일

  @Column(nullable = false)
  private String password;  // 사용자 비밀번호

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

}
