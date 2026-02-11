package com.plat.platdata.adapter.user;

import com.plat.platdata.domain.user.Authentication;
import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;
import com.plat.platdata.jparepository.AuthenticationRepository;
import com.plat.platdata.jparepository.UserRepository;
import com.plat.platdata.repository.AuthenticationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthenticationStoreAdapter implements AuthenticationStore {

    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;

    @Override
    public Authentication save(Authentication auth) {
        var userEntity = userRepository.getReferenceById(auth.getUserId() != null
            ? auth.getUserId()
            : auth.getUser().getId());

        var entity = toEntity(auth, userEntity);
        var saved = authenticationRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByEmail(String email) {
        return authenticationRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authentication> findByEmailAndAuthType(String email, AuthType authType) {
        return authenticationRepository.findByEmailAndAuthType(email, authType)
            .map(this::toDomainWithUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Authentication> findByProviderAndProviderId(Provider provider, String providerId) {
        return authenticationRepository.findByProviderAndProviderId(provider, providerId)
            .map(this::toDomainWithUser);
    }

    private com.plat.platdata.entity.user.Authentication toEntity(Authentication domain,
                                                                   com.plat.platdata.entity.user.User userEntity) {
        return com.plat.platdata.entity.user.Authentication.builder()
            .user(userEntity)
            .authType(domain.getAuthType())
            .provider(domain.getProvider())
            .providerId(domain.getProviderId())
            .email(domain.getEmail())
            .password(domain.getPassword())
            .build();
    }

    private Authentication toDomain(com.plat.platdata.entity.user.Authentication entity) {
        return Authentication.builder()
            .id(entity.getId())
            .userId(entity.getUser().getId())
            .authType(entity.getAuthType())
            .provider(entity.getProvider())
            .providerId(entity.getProviderId())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .build();
    }

    private Authentication toDomainWithUser(com.plat.platdata.entity.user.Authentication entity) {
        return Authentication.builder()
            .id(entity.getId())
            .user(UserStoreAdapter.toDomain(entity.getUser()))
            .userId(entity.getUser().getId())
            .authType(entity.getAuthType())
            .provider(entity.getProvider())
            .providerId(entity.getProviderId())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .build();
    }
}
