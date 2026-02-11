package com.plat.platdata.jparepository;

import com.plat.platdata.entity.credit.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
