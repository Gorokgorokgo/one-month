package com.example.Backend.config;

import com.example.Backend.JwtSecurityAuth;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtSecurityAuth jwtSecurityAuth;

  public SecurityConfig(JwtSecurityAuth jwtSecurityAuth) {
    this.jwtSecurityAuth = jwtSecurityAuth;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(new JwtAuthenticationFilter(jwtSecurityAuth), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/login", "/signup").permitAll()
        .anyRequest().authenticated();
  }
}
