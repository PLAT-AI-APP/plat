package com.plat.platdata.repository;

import com.plat.platdata.entity.user.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {
}
