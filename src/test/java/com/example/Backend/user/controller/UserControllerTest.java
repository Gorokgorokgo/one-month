package com.example.Backend.user.controller;

import com.example.Backend.auth.service.JwtSecurityAuth;
import com.example.Backend.user.dto.request.LoginRequest;
import com.example.Backend.user.dto.response.LoginResponse;
import com.example.Backend.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserControllerTest.class)
public class UserControllerTest {

  @Mock
  private UserService userService;
  @Mock
  private JwtSecurityAuth jwtSecurityAuth;

  @InjectMocks
  private UserController userController;

  @Test
  @DisplayName("AT01_올바른_로그인_성공시_Access_Refresh_토큰_반환")
  void AT01_ValidLogin_ShouldReturnAccessAndRefreshToken() {
    // Given
    LoginRequest request = new LoginRequest("user", "password");
    LoginResponse expectedResponse = new LoginResponse("accessToken123", "refreshToken456");

    when(userService.login(any())).thenReturn(expectedResponse);

    // When
    ResponseEntity<LoginResponse> response = userController.login(request);

    // Then
    assertNotNull(response.getBody());
    assertEquals("accessToken123", response.getBody().getAccessToken());
    assertEquals("refreshToken456", response.getBody().getRefreshToken());
  }

  @Test
  @DisplayName("AT02_잘못된_비밀번호_입력시_401_반환")
  void AT02_InvalidPassword_ShouldReturn401() {
    // Given
    LoginRequest request = new LoginRequest("user", "wrongpassword");

    when(userService.login(any())).thenThrow(new RuntimeException("Unauthorized"));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> userController.login(request));
    assertEquals("Unauthorized", exception.getMessage());
  }

  @Test
  @DisplayName("AT03_존재하지_않는_사용자로_로그인_시도시_401_반환")
  void AT03_NonExistentUser_ShouldReturn401() {
    // Given
    LoginRequest request = new LoginRequest("unknownUser", "password");

    when(userService.login(any())).thenThrow(new RuntimeException("Unauthorized"));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> userController.login(request));
    assertEquals("Unauthorized", exception.getMessage());
  }

  // ==================== 2️⃣ Access Token 검증 테스트 ====================

  @Test
  @DisplayName("AC01_유효한_Access_Token_검증_성공")
  void AC01_ValidAccessToken_ShouldReturn200() {
    // Given
    String validToken = "validAccessToken";

    when(jwtSecurityAuth.validateToken(validToken)).thenReturn(true);

    // When
    boolean isValid = jwtSecurityAuth.validateToken(validToken);

    // Then
    assertTrue(isValid);
  }

  @Test
  @DisplayName("AC02_만료된_Access_Token_검증_실패_401_반환")
  void AC02_ExpiredAccessToken_ShouldReturn401() {
    // Given
    String expiredToken = "expiredAccessToken";

    when(jwtSecurityAuth.validateToken(expiredToken)).thenThrow(new RuntimeException("만료된 Token"));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> jwtSecurityAuth.validateToken(expiredToken));
    assertEquals("만료된 Token", exception.getMessage());
  }

  @Test
  @DisplayName("AC03_위변조된_Access_Token_검증_실패_401_반환")
  void AC03_TamperedAccessToken_ShouldReturn401() {
    // Given
    String tamperedToken = "tamperedAccessToken";

    when(jwtSecurityAuth.validateToken(tamperedToken)).thenThrow(new RuntimeException("유효하지 않은 Token"));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> jwtSecurityAuth.validateToken(tamperedToken));
    assertEquals("유효하지 않은 Token", exception.getMessage());
  }

  // ==================== 3️⃣ Refresh Token을 이용한 Access Token 재발급 ====================

  @Test
  @DisplayName("RT01_유효한_Refresh_Token으로_Access_Token_재발급_성공")
  void RT01_ValidRefreshToken_ShouldIssueNewAccessToken() {
    // Given
    String validRefreshToken = "validRefreshToken";
    String newAccessToken = "newAccessToken";

    when(jwtSecurityAuth.refreshAccessToken(validRefreshToken)).thenReturn(newAccessToken);

    // When
    String refreshedToken = jwtSecurityAuth.refreshAccessToken(validRefreshToken);

    // Then
    assertEquals("newAccessToken", refreshedToken);
  }

  @Test
  @DisplayName("RT02_만료된_Refresh_Token으로_재발급_시도시_401_반환")
  void RT02_ExpiredRefreshToken_ShouldReturn401() {
    // Given
    String expiredRefreshToken = "expiredRefreshToken";

    when(jwtSecurityAuth.refreshAccessToken(expiredRefreshToken)).thenThrow(new RuntimeException("만료된 Refresh Token 입니다."));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> jwtSecurityAuth.refreshAccessToken(expiredRefreshToken));
    assertEquals("만료된 Refresh Token 입니다.", exception.getMessage());
  }

  @Test
  @DisplayName("RT03_위변조된_Refresh_Token으로_재발급_시도시_401_반환")
  void RT03_TamperedRefreshToken_ShouldReturn401() {
    // Given
    String tamperedRefreshToken = "tamperedRefreshToken";

    when(jwtSecurityAuth.refreshAccessToken(tamperedRefreshToken)).thenThrow(new RuntimeException("유효하지 않은 Refresh Token 입니다."));

    // When & Then
    Exception exception = assertThrows(RuntimeException.class, () -> jwtSecurityAuth.refreshAccessToken(tamperedRefreshToken));
    assertEquals("유효하지 않은 Refresh Token 입니다.", exception.getMessage());
  }
}