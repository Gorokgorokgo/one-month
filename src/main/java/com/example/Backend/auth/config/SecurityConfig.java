package com.example.Backend.auth.config;

import com.example.Backend.auth.service.JwtAuthenticationFilter;
import com.example.Backend.auth.service.JwtSecurityAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtSecurityAuth jwtSecurityAuth;

  public SecurityConfig(JwtSecurityAuth jwtSecurityAuth) {
    this.jwtSecurityAuth = jwtSecurityAuth;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .addFilterBefore(new JwtAuthenticationFilter(jwtSecurityAuth), UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/signup",
                "api/login").permitAll()  // 회원가입은 누구나 접근 가능
            .anyRequest().authenticated()                    // 그 외의 요청은 인증이 필요
        );

    return http.build();
  }

  // BCryptPasswordEncoder를 빈으로 등록
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
