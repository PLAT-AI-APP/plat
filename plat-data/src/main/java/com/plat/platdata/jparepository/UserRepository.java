package com.plat.platdata.jparepository;

import com.plat.platdata.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);
}
