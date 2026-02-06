package com.plat.platdata.repository;

import com.plat.platdata.entity.credit.CreditBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditBalanceRepository extends JpaRepository<CreditBalance, Long> {
}
