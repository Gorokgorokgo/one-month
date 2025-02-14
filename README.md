## Filter란 무엇인가?
- Servlet 수준에서 동작하는 요청/응답 가로채기 기능
- HTTP 요청이 서블릿에 도달하기 전에 실행됨
- Spring Security 같은 인증/인가 처리에서 많이 사용됨


## Interceptor (Spring MVC HandlerInterceptor)
- Spring MVC에서 컨트롤러에 도달하기 전/후에 실행됨
- 요청을 컨트롤러로 보내기 전에 추가 검증을 하거나, 응답을 변경할 수 있음
- 주로 로그인 인증, 권한 체크, 로깅 등에 사용


## AOP (Aspect-Oriented Programming, 관점 지향 프로그래밍)
- 메서드 실행 전/후에 특정 로직을 삽입할 수 있는 방식
- 주로 트랜잭션 관리, 로깅, 권한 검사 등에 사용됨
- Filter, Interceptor와 다르게 Spring Bean 내부의 메서드 실행을 가로챌 수 있음  


    

---

  
  

| 비교 항목       | Filter                         | Interceptor                  | AOP (Aspect-Oriented Programming) |
|---------------|--------------------------------|------------------------------|----------------------------------|
| **동작 시점**  | 서블릿 요청 전/후              | 컨트롤러 실행 전/후           | 메서드 실행 전/후, 예외 발생 시  |
| **적용 대상**  | HTTP 요청/응답                 | Spring MVC 요청/응답         | Spring Bean 메서드 호출         |
| **주요 목적**  | 인증/인가, 로깅, CORS 설정    | 인증/인가, 로깅, 세션 검증    | 트랜잭션 관리, 로깅, 공통 관심사 분리 |
| **사용 방법**  | `javax.servlet.Filter` 구현   | `HandlerInterceptor` 구현    | `@Aspect` 및 `@Around` 사용    |
| **실행 순서**  | 클라이언트 → Filter → DispatcherServlet → Interceptor → Controller | 클라이언트 → DispatcherServlet → Interceptor → Controller | AOP는 메서드 실행을 가로채서 동작 |

---
### Spring Security란?
- Spring 기반 애플리케이션의 보안을 관리하는 프레임 워크  
  -> 로그인, 인증(사용자 확인), 인가(권한 확인), 요청 검사, 세션 관리등을 자동으로 처리해줌
---

### JWT란 (Json Web Token)
- Json기반의 토큰으로, 클라이언트와 서버간의 인증 및 정보를 안전하게 교환하는데 사용된다.

---

### ✅ Access / Refresh Token 발행 및 검증 테스트 시나리오  

---

## 1️⃣ Access / Refresh Token 발행 테스트  
**🎯 시나리오:**  
- 사용자가 올바른 로그인 정보를 제공하면 Access Token과 Refresh Token이 정상적으로 발행되는지 검증  

**📝 테스트 케이스:**  

| 테스트 ID | 설명 | 입력 값 | 예상 결과 |
|----------|------|---------|-----------|
| AT01 | 올바른 로그인 시 JWT 발급 | 유효한 사용자 ID, PW | Access & Refresh Token 반환 |
| AT02 | 잘못된 비밀번호 입력 시 실패 | 잘못된 PW | 401 Unauthorized 반환 |
| AT03 | 존재하지 않는 사용자로 요청 | 미등록 ID | 401 Unauthorized 반환 |

---


## 2️⃣ Access Token 검증 테스트  
**🎯 시나리오:**  
- 클라이언트가 제공한 Access Token이 유효한지 검증  

**📝 테스트 케이스:**  

| 테스트 ID | 설명 | 입력 값 | 예상 결과 |
|----------|------|---------|-----------|
| AC01 | 유효한 Access Token 검증 | 정상적인 Access Token | 200 OK 반환 |
| AC02 | 만료된 Access Token 검증 | 만료된 Token | 401 Unauthorized 반환 |
| AC03 | 변조된 Access Token 검증 | 위·변조된 Token | 401 Unauthorized 반환 |


---

## 3️⃣ Refresh Token을 이용한 Access Token 재발급 테스트  
**🎯 시나리오:**  
- 만료된 Access Token을 Refresh Token으로 재발급하는 과정 테스트  

**📝 테스트 케이스:**  

| 테스트 ID | 설명 | 입력 값 | 예상 결과 |
|----------|------|---------|-----------|
| RT01 | 유효한 Refresh Token으로 재발급 | 정상적인 Refresh Token | 새로운 Access Token 반환 |
| RT02 | 만료된 Refresh Token으로 재발급 시도 | 만료된 Refresh Token | 401 Unauthorized 반환 |
| RT03 | 변조된 Refresh Token으로 재발급 시도 | 위·변조된 Token | 401 Unauthorized 반환 |

---






