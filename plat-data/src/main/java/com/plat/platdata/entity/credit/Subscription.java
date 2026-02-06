package com.plat.platdata.entity.credit;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.chat.enums.PlanType;
import com.plat.platdata.entity.credit.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "auto_renew", nullable = false)
    private Boolean autoRenew;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Builder
    public Subscription(Long userId, PlanType planType, LocalDate startDate, LocalDate endDate,
                        Boolean autoRenew, SubscriptionStatus status) {
        this.userId = userId;
        this.planType = planType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.autoRenew = autoRenew;
        this.status = status != null ? status : SubscriptionStatus.ACTIVE;
    }
}
