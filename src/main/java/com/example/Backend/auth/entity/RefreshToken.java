package com.example.Backend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class RefreshToken {

  @Id
  private String token;

  private String username;

}
