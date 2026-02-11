package com.plat.platboot.auth.oauth2;

import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.repository.AuthenticationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final AuthenticationStore authenticationStore;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Authentication authentication = authenticationStore.findByEmailAndAuthType(email, AuthType.EMAIL)
            .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다."));
        return new PrincipalDetails(authentication.getUser(), authentication);
    }
}
