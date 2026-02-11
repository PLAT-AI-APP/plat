package com.plat.platdata.auth.jwt;

import com.plat.platdata.auth.dto.SignupClaims;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class SignupTokenProvider {

    private static final long SIGNUP_TOKEN_EXPIRATION_MS = 10 * 60 * 1000L; // 10분

    private final SecretKey secretKey;

    public String generateEmailSignupToken(String email, String encodedPassword) {
        Date now = new Date();
        return Jwts.builder()
            .issuer("PLAT_SIGNUP")
            .claim("authType", AuthType.EMAIL.name())
            .claim("email", email)
            .claim("encodedPassword", encodedPassword)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + SIGNUP_TOKEN_EXPIRATION_MS))
            .signWith(secretKey)
            .compact();
    }

    public String generateSocialSignupToken(Provider provider, String providerId) {
        Date now = new Date();
        return Jwts.builder()
            .issuer("PLAT_SIGNUP")
            .claim("authType", AuthType.valueOf(provider.name()).name())
            .claim("provider", provider.name())
            .claim("providerId", providerId)
            .issuedAt(now)
            .expiration(new Date(now.getTime() + SIGNUP_TOKEN_EXPIRATION_MS))
            .signWith(secretKey)
            .compact();
    }

    public SignupClaims validateSignupToken(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        AuthType authType = AuthType.valueOf(claims.get("authType", String.class));

        return new SignupClaims(
            authType,
            claims.get("email", String.class),
            claims.get("encodedPassword", String.class),
            claims.get("provider", String.class) != null
                ? Provider.valueOf(claims.get("provider", String.class)) : null,
            claims.get("providerId", String.class)
        );
    }
}
