package com.plat.platboot.auth.oauth2.oauth;

import com.plat.platboot.auth.oauth2.PrincipalDetails;
import com.plat.platdata.auth.jwt.JwtTokenProvider;
import com.plat.platdata.auth.jwt.SignupTokenProvider;
import com.plat.platdata.auth.jwt.TokenId;
import com.plat.platdata.domain.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.plat.platdata.auth.jwt.JwtTokenType.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final SignupTokenProvider signupTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if (principalDetails.isNewUser()) {
            // 신규 유저 → signupToken 발급 (DB 저장 없음)
            String signupToken = signupTokenProvider.generateSocialSignupToken(
                principalDetails.getProvider(), principalDetails.getProviderId());

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getOutputStream(),
                Map.of("signupToken", signupToken, "newUser", true));
        } else {
            // 기존 유저 → JWT 쿠키 발급
            User user = principalDetails.getUser();
            TokenId tokenId = new TokenId(principalDetails.getProvider().name(), principalDetails.getProviderId());

            var accessToken = tokenProvider.generateToken(ACCESS_TOKEN, tokenId, user.getId(), List.of(user.getRole()));
            var refreshToken = tokenProvider.generateToken(REFRESH_TOKEN, tokenId, user.getId(), List.of(user.getRole()));

            response.addHeader(SET_COOKIE, accessToken.toString());
            response.addHeader(SET_COOKIE, refreshToken.toString());
        }

        // OAuth2 로그인 시 세션을 초기화한다.
        removeJsessionId(request, response);
    }

    private static void removeJsessionId(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);
    }
}
