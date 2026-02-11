package com.plat.platdata.adapter.user;

import com.plat.platdata.domain.user.User;
import com.plat.platdata.jparepository.UserRepository;
import com.plat.platdata.repository.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStoreAdapter implements UserStore {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        var entity = toEntity(user);
        var saved = userRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private com.plat.platdata.entity.user.User toEntity(User domain) {
        return com.plat.platdata.entity.user.User.builder()
            .nickname(domain.getNickname())
            .birth(domain.getBirth())
            .gender(domain.getGender())
            .role(domain.getRole())
            .profileImage(domain.getProfileImage())
            .phone(domain.getPhone())
            .adultVerified(domain.getAdultVerified())
            .build();
    }

    static User toDomain(com.plat.platdata.entity.user.User entity) {
        return User.builder()
            .id(entity.getId())
            .nickname(entity.getNickname())
            .birth(entity.getBirth())
            .gender(entity.getGender())
            .role(entity.getRole())
            .profileImage(entity.getProfileImage())
            .phone(entity.getPhone())
            .adultVerified(entity.getAdultVerified())
            .build();
    }
}
