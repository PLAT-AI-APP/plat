package com.plat.platdata.auth.jwt;

import com.plat.platdata.auth.dto.UserAuthToken;
import com.plat.platdata.domain.user.enums.Role;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public ResponseCookie generateToken(JwtTokenType tokenType, TokenId tokenId , long userId,
                                        List<Role> authorities) {
        String token = createToken(tokenType, tokenId, userId, authorities);
        String path = getPath(tokenType);
        return createCookie(tokenType.getCookieName(), token, tokenType.getExpiration(), path);
    }

    public ResponseCookie removeToken(JwtTokenType tokenType) {
        String path = getPath(tokenType);
        return createCookie(tokenType.getCookieName(), null, 0, path);
    }

    private String getPath(JwtTokenType tokenType) {
        return tokenType.getPath();
    }


    private ResponseCookie createCookie(String cookieName, @Nullable String token, int expiration, String path) {
        return ResponseCookie.from(cookieName, token)
            .path(path)
            .sameSite("Lax")
            .httpOnly(true)
            .secure(false)
            .maxAge(expiration)
            .build();
    }
    // 토큰 생성
    private String createToken(JwtTokenType tokenType, TokenId tokenId,
                               Long userId,
                               List<Role> authorities) {
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
            .header()
            .keyId(UUID.randomUUID().toString())
            .add("typ", "JWT")
            .and()
            .issuer("PLAT_JWT")
            .claim("iss", tokenId.provider())
            .claim("sub", tokenId.providerId())
            .claim("userId", userId)
            .claim("roles", authorities.stream()
                .map(Enum::name)
                .toList())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + tokenType.getExpiration() * 1000L))
            .signWith(secretKey)
            .compact();
    }

    public List<ResponseCookie> refresh(UserAuthToken userAuthToken) {
        TokenId tokenId = new TokenId(userAuthToken.provider(), userAuthToken.providerId());

        var accessToken = generateToken(JwtTokenType.ACCESS_TOKEN, tokenId, userAuthToken.userId(), userAuthToken.roles());
        var refreshToken = generateToken(JwtTokenType.REFRESH_TOKEN, tokenId, userAuthToken.userId(), userAuthToken.roles());

        return List.of(accessToken, refreshToken);
    }
}
