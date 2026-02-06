package com.plat.platdata.repository;

import com.plat.platdata.entity.credit.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
