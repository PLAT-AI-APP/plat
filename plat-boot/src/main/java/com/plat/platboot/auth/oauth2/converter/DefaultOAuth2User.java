package com.plat.platboot.auth.oauth2.converter;

import com.plat.platdata.domain.user.enums.Provider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultOAuth2User {
    private final Provider provider;
    private final String providerId;
    private final String name;

}