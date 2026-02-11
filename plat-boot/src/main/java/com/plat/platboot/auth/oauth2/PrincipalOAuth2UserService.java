package com.plat.platboot.auth.oauth2;

import com.plat.platboot.auth.oauth2.converter.DefaultOAuth2User;
import com.plat.platboot.auth.oauth2.converter.OAuth2Converter;
import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.enums.Provider;
import com.plat.platdata.repository.AuthenticationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationStore authenticationStore;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Provider provider = Provider.findByProvider(userRequest.getClientRegistration().getClientName());
        DefaultOAuth2User userInfo = OAuth2Converter.of(provider).convert(oAuth2User);

        Optional<Authentication> existingAuth = authenticationStore
            .findByProviderAndProviderId(provider, userInfo.getProviderId());

        if (existingAuth.isPresent()) {
            Authentication authentication = existingAuth.get();
            return new PrincipalDetails(authentication.getUser(), authentication, provider);
        }

        // 신규 유저 — DB에 저장하지 않고 임시 PrincipalDetails 반환
        return new PrincipalDetails(provider, userInfo.getProviderId());
    }
}
