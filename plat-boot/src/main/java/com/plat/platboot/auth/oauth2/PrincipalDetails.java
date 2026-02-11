package com.plat.platboot.auth.oauth2;

import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.User;
import com.plat.platdata.domain.user.enums.Provider;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user;
    private final Authentication authentication;
    private final boolean newUser;

    // OAuth2 신규 유저 (DB 미저장 상태)
    private final Provider provider;
    private final String providerId;

    // 기존 이메일 유저
    public PrincipalDetails(User user, Authentication authentication) {
        this.user = user;
        this.authentication = authentication;
        this.newUser = false;
        this.provider = null;
        this.providerId = null;
    }

    // 기존 OAuth2 유저
    public PrincipalDetails(User user, Authentication authentication, Provider provider) {
        this.user = user;
        this.authentication = authentication;
        this.newUser = false;
        this.provider = provider;
        this.providerId = authentication.getProviderId();
    }

    // 신규 OAuth2 유저 (DB 미저장)
    public PrincipalDetails(Provider provider, String providerId) {
        this.user = null;
        this.authentication = null;
        this.newUser = true;
        this.provider = provider;
        this.providerId = providerId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(user.getRole().getRole()));
    }

    @Override
    public String getUsername() {
        if (authentication != null) {
            return authentication.getEmail();
        }
        return null;
    }

    @Override
    public String getPassword() {
        if (authentication != null) {
            return authentication.getPassword();
        }
        return null;
    }

    @Override
    public String getName() {
        if (user != null) {
            return user.getNickname();
        }
        return providerId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
