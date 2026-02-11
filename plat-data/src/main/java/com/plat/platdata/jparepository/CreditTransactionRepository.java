package com.plat.platdata.jparepository;

import com.plat.platdata.entity.credit.CreditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {
}
