package com.example.Backend.user.service;

import com.example.Backend.auth.service.JwtSecurityAuth;
import com.example.Backend.user.dto.request.LoginRequest;
import com.example.Backend.user.dto.request.SignupRequest;
import com.example.Backend.user.dto.response.LoginResponse;
import com.example.Backend.user.dto.response.SignupResponse;
import com.example.Backend.user.entity.User;
import com.example.Backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final JwtSecurityAuth jwtSecurityAuth;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  // 유저 회원가입
  @Transactional
  public SignupResponse signup(SignupRequest signupRequest) {

    String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

    // 유저(회원) 생성
    User newUser = new User(signupRequest.getUsername(), signupRequest.getEmail(), encodedPassword);
    userRepository.save(newUser);
    return new SignupResponse(newUser.getEmail(), newUser.getPassword()); //회원 저장
  }

  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {

    // 사용자 정보 조회
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

    // 2. 비밀번호 비교
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new RuntimeException("잘못된 비밀번호입니다.");
    }

    // 3. 액세스 토큰 생성
    String accessToken = jwtSecurityAuth.generateToken(user.getUsername(), true);  // 액세스 토큰은 true

    // 4. 리프레시 토큰 생성 (로그인 후 저장하거나 별도 처리 가능)
    String refreshToken = jwtSecurityAuth.generateToken(user.getUsername(), false);  // 리프레시 토큰은 false

    // 5. LoginResponse 반환
    return new LoginResponse(accessToken, refreshToken);
  }
}
