# Security Architecture

## 개요

Spring Security + OAuth2 Resource Server + JWT 기반 인증 구조.
SPA(프론트엔드) + REST API(백엔드) 아키텍처에서 `Authorization: Bearer <token>` 헤더 방식으로 인증을 처리한다.

세션을 사용하지 않으며(Stateless), 모든 인증 상태는 JWT 토큰에 담긴다.

## 모듈별 역할

```
plat-data   : 공통 JWT 인프라 (검증, 파싱, 디코딩, 공통 타입)
plat-boot   : 인증 주체 (로그인, JWT 생성, 로그아웃)
plat-ai     : JWT 검증만 (토큰이 유효한지 확인)
```

### 의존 관계

```
plat-boot ──→ plat-data (security, oauth2-resource-server, jjwt 전이 의존성)
plat-ai   ──→ plat-data (security, oauth2-resource-server, jjwt 전이 의존성)
```

plat-data에서 security 관련 의존성을 관리하므로 plat-boot, plat-ai는 동일한 security 버전 환경을 공유한다.

### 모듈별 의존성

| 의존성 | plat-data | plat-boot | plat-ai |
|--------|-----------|-----------|---------|
| `spring-boot-starter-security` | O | 전이 | 전이 |
| `spring-boot-starter-oauth2-resource-server` | O | 전이 | 전이 |
| `spring-boot-starter-oauth2-client` | X | O (소셜 로그인) | X |
| `jjwt` | O | 전이 | 전이 |

## 인증 흐름

### 1. 로그인 (plat-boot)

```
[이메일 로그인]
클라이언트 → POST /login (JSON: username, password)
           → JsonAuthenticationFilter
           → PrincipalDetailsService (DB 조회)
           → 인증 성공 → JwtTokenProvider.generateToken()
           → Authorization 헤더로 JWT 응답

[소셜 로그인]
클라이언트 → OAuth2 로그인 요청
           → PrincipalOAuth2UserService (소셜 사용자 처리)
           → OAuth2AuthenticationSuccessHandler
           → JwtTokenProvider.generateToken()
           → JWT 응답
```

### 2. 인증된 요청 처리 (plat-boot, plat-ai 공통)

```
클라이언트 → Authorization: Bearer <token>
           → Spring Security oauth2ResourceServer
           → JwtDecoder (토큰 서명 검증 + 만료 확인)
           → CustomJwtAuthenticationConverter
           → CustomJwtToken (userId, authorities 포함)
           → SecurityContext에 저장
```

### 3. 컨트롤러에서 인증 정보 접근

```java
// 방법 1: JwtUserContextHolder (어디서든)
CustomJwtToken token = JwtUserContextHolder.getJwtToken();
Long userId = token.getUserId();

// 방법 2: @JwtToken 어노테이션 (컨트롤러 파라미터)
@GetMapping("/example")
public ResponseEntity<?> example(@JwtToken CustomJwtToken token) {
    Long userId = token.getUserId();
}
```

## 패키지 구조

### plat-data (`com.plat.platdata.auth`)

공통 JWT 인프라. plat-boot, plat-ai 모두 이 패키지의 클래스를 사용한다.

```
auth/
├── dto/
│   └── UserAuthToken          # 인증 사용자 정보 DTO
└── jwt/
    ├── JwtConfig              # SecretKey, JwtDecoder 빈 설정
    ├── JwtTokenProvider       # JWT 생성/삭제
    ├── JwtTokenType           # ACCESS_TOKEN, REFRESH_TOKEN 정의
    ├── TokenId                # provider + providerId 식별자
    ├── CustomJwtToken         # JwtAuthenticationToken 확장 (userId 포함)
    ├── CustomJwtAuthenticationConverter  # JWT → CustomJwtToken 변환
    └── annotation/
        ├── @JwtToken          # 컨트롤러 파라미터 어노테이션
        ├── JwtTokenArgumentResolver     # @JwtToken 리졸버
        └── JwtUserContextHolder         # SecurityContext에서 CustomJwtToken 조회
```

### plat-boot (`com.plat.platboot.auth`)

로그인, JWT 생성, 로그아웃 처리. 인증의 주체.

```
auth/
├── config/
│   └── SecurityConfig                   # SecurityFilterChain (로그인 + OAuth2 + 리소스 서버)
├── oauth2/
│   ├── PrincipalDetails                 # UserDetails + OAuth2User 구현체
│   ├── PrincipalDetailsService          # 이메일 로그인 시 DB 사용자 조회
│   ├── PrincipalOAuth2UserService       # 소셜 로그인 시 사용자 처리
│   ├── converter/
│   │   ├── OAuth2Converter              # 소셜 로그인 제공자별 변환 인터페이스
│   │   ├── OAuth2KakaoConverter         # 카카오 사용자 정보 변환
│   │   └── DefaultOAuth2User            # 변환된 소셜 사용자 DTO
│   └── oauth/
│       └── OAuth2AuthenticationSuccessHandler  # 소셜 로그인 성공 → JWT 발급
├── formLogin/
│   └── JsonAuthenticationFilter         # JSON 이메일 로그인 필터
└── logout/
    ├── JwtTokenClearingLogoutHandler    # 로그아웃 시 토큰 제거
    └── JsonLogoutSuccessHandler         # 로그아웃 성공 JSON 응답
```

### plat-ai (`com.plat.platai.security`)

JWT 검증만 담당. 로그인이나 토큰 생성 로직 없음.

```
security/
└── SecurityConfig              # SecurityFilterChain (stateless, JWT 검증만)
```

## JWT 토큰 구조

```
Header:  { "typ": "JWT", "kid": "<UUID>" }
Payload: {
    "iss": "<provider>",
    "sub": "<providerId>",
    "userId": <Long>,
    "scope": ["USER"] 또는 ["CREATOR"] 또는 ["ADMIN"],
    "iat": <발급시간>,
    "exp": <만료시간>
}
```

### 토큰 종류

| 토큰 | 만료 시간 | 용도 |
|------|----------|------|
| ACCESS_TOKEN | 30분 | API 요청 인증 |
| REFRESH_TOKEN | 7일 | Access Token 갱신 |

## Role 체계

| Role | 설명 |
|------|------|
| `USER` | 일반 사용자 |
| `CREATOR` | 캐릭터 생성자 |
| `ADMIN` | 관리자 |

> 관리자 세부 권한 (SUPER_ADMIN, CS_ADMIN 등)은 추후 확정 예정

## SecurityFilterChain 비교

### plat-boot

| 항목 | 설정 |
|------|------|
| 세션 | STATELESS |
| CSRF | 비활성 (SPA) |
| 이메일 로그인 | JsonAuthenticationFilter (`/login`) |
| 소셜 로그인 | oauth2Login (카카오 등) |
| JWT 검증 | oauth2ResourceServer + CustomJwtAuthenticationConverter |
| 로그아웃 | `/auth/logout` |
| 토큰 리졸버 | DefaultBearerTokenResolver (Authorization 헤더) |

### plat-ai

| 항목 | 설정 |
|------|------|
| 세션 | STATELESS |
| CSRF | 비활성 (SPA) |
| 이메일 로그인 | 없음 |
| 소셜 로그인 | 없음 |
| JWT 검증 | oauth2ResourceServer + CustomJwtAuthenticationConverter |
| 로그아웃 | 없음 |
| 토큰 리졸버 | DefaultBearerTokenResolver (Authorization 헤더) |

## 설정

`application.yaml`에 JWT 시크릿 키 설정이 필요하다.

```yaml
security:
  jwt:
    secret: # HMAC-SHA512 시크릿 키
```

## 새 모듈에서 Security 적용하기

1. `plat-data`를 의존성에 추가하면 security, oauth2-resource-server, jjwt가 전이 의존성으로 포함된다
2. `SecurityFilterChain` 빈을 정의한다
3. `oauth2ResourceServer`에 `CustomJwtAuthenticationConverter`를 연결한다
4. 컨트롤러에서 `@JwtToken` 또는 `JwtUserContextHolder`로 인증 정보에 접근한다

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter()))
            )
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .build();
    }
}
```