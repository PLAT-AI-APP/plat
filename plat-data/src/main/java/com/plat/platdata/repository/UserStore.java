package com.plat.platdata.repository;

import com.plat.platdata.domain.user.User;

public interface UserStore {

    User save(User user);

    boolean existsByNickname(String nickname);
}
