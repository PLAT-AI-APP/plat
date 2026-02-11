package com.plat.platdata.jparepository;

import com.plat.platdata.entity.credit.Credit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditRepository extends JpaRepository<Credit, Long> {
}
