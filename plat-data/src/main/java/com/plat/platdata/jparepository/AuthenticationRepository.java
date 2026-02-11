package com.plat.platdata.jparepository;

import com.plat.platdata.entity.user.Authentication;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

    Optional<Authentication> findByProviderAndProviderId(Provider provider, String providerId);

    Optional<Authentication> findByEmailAndAuthType(String email, AuthType authType);

    boolean existsByEmail(String email);
}
