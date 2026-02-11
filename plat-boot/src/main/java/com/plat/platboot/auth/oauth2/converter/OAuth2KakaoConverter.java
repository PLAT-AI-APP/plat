package com.plat.platboot.auth.oauth2.converter;

import com.plat.platdata.domain.user.enums.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public final class OAuth2KakaoConverter implements OAuth2Converter {

    @Override
    public DefaultOAuth2User convert(OAuth2User user) {
        Map<String, Object> properties = user.getAttribute("properties");
        String nickname = (String) properties.get("nicknamke");
        if (nickname == null) {
            nickname = generateRandomNickname(Provider.KAKAO);
        }
        return DefaultOAuth2User.builder()
            .provider(Provider.KAKAO)
            .providerId(user.getName())
            .name(nickname)
            .build();
    }
}