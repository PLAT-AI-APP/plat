package com.plat.platboot.auth.service;

import com.plat.platboot.auth.dto.SignupTokenResponse;
import com.plat.platdata.auth.dto.SignupClaims;
import com.plat.platdata.auth.jwt.JwtTokenProvider;
import com.plat.platdata.auth.jwt.JwtTokenType;
import com.plat.platdata.auth.jwt.SignupTokenProvider;
import com.plat.platdata.auth.jwt.TokenId;
import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.User;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Gender;
import com.plat.platdata.domain.user.enums.Role;
import com.plat.platdata.repository.AuthenticationStore;
import com.plat.platdata.repository.UserStore;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserStore userStore;
    private final AuthenticationStore authenticationStore;
    private final SignupTokenProvider signupTokenProvider;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public SignupTokenResponse initiateEmailSignup(String email, String password) {
        if (authenticationStore.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String signupToken = signupTokenProvider.generateEmailSignupToken(email, encodedPassword);
        return new SignupTokenResponse(signupToken, true);
    }

    @Transactional
    public void completeSignup(String signupToken, String nickname, LocalDateTime birth,
                               Gender gender, HttpServletResponse response) {
        SignupClaims claims = signupTokenProvider.validateSignupToken(signupToken);

        if (userStore.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
            .nickname(nickname)
            .birth(birth)
            .gender(gender)
            .role(Role.USER)
            .build();
        User savedUser = userStore.save(user);

        Authentication authentication = buildAuthentication(claims, savedUser);
        authenticationStore.save(authentication);

        // JWT 발급
        TokenId tokenId = buildTokenId(claims);
        var accessToken = jwtTokenProvider.generateToken(JwtTokenType.ACCESS_TOKEN, tokenId, savedUser.getId(), List.of(savedUser.getRole()));
        var refreshToken = jwtTokenProvider.generateToken(JwtTokenType.REFRESH_TOKEN, tokenId, savedUser.getId(), List.of(savedUser.getRole()));

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
    }

    public boolean isNicknameAvailable(String nickname) {
        return !userStore.existsByNickname(nickname);
    }

    public void refreshTokens(HttpServletResponse response) {
        // TODO: JWT 갱신 로직 구현
        throw new UnsupportedOperationException("토큰 갱신 미구현");
    }

    private Authentication buildAuthentication(SignupClaims claims, User user) {
        if (claims.authType() == AuthType.EMAIL) {
            return Authentication.email(user.getId())
                .build(claims.email(), claims.encodedPassword());
        }
        return Authentication.social(user.getId())
            .build(claims.authType(), claims.provider(), claims.providerId());
    }

    private TokenId buildTokenId(SignupClaims claims) {
        if (claims.authType() == AuthType.EMAIL) {
            return new TokenId("EMAIL", claims.email());
        }
        return new TokenId(claims.provider().name(), claims.providerId());
    }
}
