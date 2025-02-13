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

