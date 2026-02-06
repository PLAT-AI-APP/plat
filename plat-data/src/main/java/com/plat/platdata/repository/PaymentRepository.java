package com.plat.platdata.repository;

import com.plat.platdata.entity.credit.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
