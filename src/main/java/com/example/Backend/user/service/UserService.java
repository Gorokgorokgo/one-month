package com.example.Backend.user.service;

import com.example.Backend.auth.dto.response.AuthorityResponse;
import com.example.Backend.auth.service.JwtSecurityAuth;
import com.example.Backend.user.dto.request.LoginRequest;
import com.example.Backend.user.dto.request.SignupRequest;
import com.example.Backend.user.dto.response.LoginResponse;
import com.example.Backend.user.dto.response.SignupResponse;
import com.example.Backend.user.entity.User;
import com.example.Backend.user.entity.UserRole;
import com.example.Backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    Set<UserRole> roles = new HashSet<>();
    roles.add(UserRole.ROLE_USER);

    // 유저(회원) 생성
    User newUser = new User(signupRequest.getUsername(), signupRequest.getNickname(), encodedPassword, roles);

    List<AuthorityResponse> authorities = newUser.getRoles().stream()
        .map(role -> new AuthorityResponse(role.name())) // 예: ROLE_USER, ROLE_ADMIN
        .toList();

    userRepository.save(newUser);
    return new SignupResponse(newUser.getUsername(), newUser.getNickname(), authorities); //회원 저장
  }

  @Transactional
  public LoginResponse login(LoginRequest loginRequest) {

    // 사용자 정보 조회
    User user = userRepository.findByUsername(loginRequest.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유저를 찾을 수 없습니다."));

    // 2. 비밀번호 비교
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
    }

    // 3. 액세스 토큰 생성
    String accessToken = jwtSecurityAuth.generateToken(user.getUsername(), true);  // 액세스 토큰은 true

    // 4. 리프레시 토큰 생성 (로그인 후 저장하거나 별도 처리 가능)
    String refreshToken = jwtSecurityAuth.generateToken(user.getUsername(), false);  // 리프레시 토큰은 false

    // 5. LoginResponse 반환
    return new LoginResponse(accessToken, refreshToken);
  }
}
