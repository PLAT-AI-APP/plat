package com.plat.platdata.jparepository;

import com.plat.platdata.entity.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
