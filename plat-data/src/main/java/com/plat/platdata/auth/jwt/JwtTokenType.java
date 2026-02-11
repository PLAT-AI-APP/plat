package com.plat.platdata.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtTokenType {

    ACCESS_TOKEN("accessToken", 60 * 30, "/"),
    REFRESH_TOKEN("refreshToken", 60 * 60 * 24 * 7, "/auth/refresh");

    private final String cookieName;
    private final int expiration;
    private final String path;

    public static boolean isTokenType(String cookieName) {
        return ACCESS_TOKEN.cookieName.equals(cookieName)
            || REFRESH_TOKEN.cookieName.equals(cookieName);
    }
}
