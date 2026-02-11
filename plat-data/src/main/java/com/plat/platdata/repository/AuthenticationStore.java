package com.plat.platdata.repository;

import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;

import java.util.Optional;

public interface AuthenticationStore {

    Authentication save(Authentication auth);

    boolean existsByEmail(String email);

    Optional<Authentication> findByEmailAndAuthType(String email, AuthType authType);

    Optional<Authentication> findByProviderAndProviderId(Provider provider, String providerId);
}
