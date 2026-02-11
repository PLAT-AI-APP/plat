package com.plat.platdata.domain.user;

import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authentication {

    private final Long id;
    private final User user;
    private final Long userId;
    private final AuthType authType;
    private final Provider provider;
    private final String providerId;
    private final String email;
    private final String password;


    public static AuthenticationEmailBuilder email(Long userId) {
        return new AuthenticationEmailBuilder(userId);
    }

    public static AuthenticationSocialBuilder social(Long userId) {
        return new AuthenticationSocialBuilder(userId);
    }


    public static class AuthenticationSocialBuilder {

        private final Long userId;

        public AuthenticationSocialBuilder(Long userId) {
            this.userId = userId;
        }

        public Authentication build(AuthType authType, Provider provider, String providerId) {
            return Authentication.builder()
                .id(userId)
                .authType(authType)
                .provider(provider)
                .providerId(providerId)
                .build();
        }
    }

    public static class AuthenticationEmailBuilder {

        private final Long userId;

        public AuthenticationEmailBuilder(Long userId) {
            this.userId = userId;
        }

        public Authentication build(String email, String encodedPassword) {
            return Authentication.builder()
                .id(userId)
                .authType(AuthType.EMAIL)
                .email(email)
                .password(encodedPassword)
                .build();
        }
    }
}
