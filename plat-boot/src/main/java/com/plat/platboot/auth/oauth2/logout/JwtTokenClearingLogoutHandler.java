package com.plat.platboot.auth.oauth2.logout;

import com.plat.platdata.auth.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.plat.platdata.auth.jwt.JwtTokenType.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RequiredArgsConstructor
public class JwtTokenClearingLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ResponseCookie accessToken = tokenProvider.removeToken(ACCESS_TOKEN);
        ResponseCookie refreshToken = tokenProvider.removeToken(REFRESH_TOKEN);

        response.addHeader(SET_COOKIE, accessToken.toString());
        response.addHeader(SET_COOKIE, refreshToken.toString());
    }
}
