package com.plat.platdata.jparepository;

import com.plat.platdata.entity.credit.CreditBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditBalanceRepository extends JpaRepository<CreditBalance, Long> {
}
