package com.plat.platboot.auth.oauth2.logout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {

        // JSON 응답으로 로그아웃 성공 알림
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> result = Map.of(
            "success", true,
            "message", "로그아웃"
        );

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}