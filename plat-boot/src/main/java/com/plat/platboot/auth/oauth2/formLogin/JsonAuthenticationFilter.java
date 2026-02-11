package com.plat.platboot.auth.oauth2.formLogin;

import com.plat.platboot.auth.oauth2.PrincipalDetails;
import com.plat.platdata.auth.jwt.JwtTokenProvider;
import com.plat.platdata.auth.jwt.TokenId;
import com.plat.platdata.domain.user.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.plat.platdata.auth.jwt.JwtTokenType.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider tokenProvider;

    public JsonAuthenticationFilter(ObjectMapper objectMapper, JwtTokenProvider tokenProvider) {
        super();
        this.objectMapper = objectMapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            JsonNode jsonNode = objectMapper.readTree(request.getInputStream());

            String email = jsonNode.get("email").asString();
            String password = jsonNode.get("password").asString();

            // 인증 객체 생성
            var authToken = new UsernamePasswordAuthenticationToken(email, password);

            // AuthenticationManager 에 인증 위임
            return getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        User user = principalDetails.getUser();
        TokenId tokenId = new TokenId("EMAIL", principalDetails.getUsername());

        // JWT 토큰 생성
        var accessToken = tokenProvider.generateToken(ACCESS_TOKEN, tokenId, user.getId(), List.of(user.getRole()));
        var refreshToken = tokenProvider.generateToken(REFRESH_TOKEN, tokenId, user.getId(), List.of(user.getRole()));

        response.addHeader(SET_COOKIE, accessToken.toString());
        response.addHeader(SET_COOKIE, refreshToken.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "아이디 또는 비밀번호가 틀렸습니다.");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
