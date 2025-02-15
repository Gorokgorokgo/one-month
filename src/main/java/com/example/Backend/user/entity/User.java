package com.example.Backend.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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
  private String nickname;  // 사용자 닉네임

  @Column(nullable = false)
  private String password;  // 사용자 비밀번호

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Set<UserRole> roles = new HashSet<>();


  public User(String username, String nickname, String password, Set<UserRole> roles) {
    this.username = username;
    this.nickname = nickname;
    this.password = password;
    this.roles = roles;
  }

  public void addRole(UserRole role) {
    this.roles.add(role);
  }

}
