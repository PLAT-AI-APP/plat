package com.plat.platdata.repository;

import com.plat.platdata.entity.user.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRepository extends JpaRepository<Verification, Long> {
}
