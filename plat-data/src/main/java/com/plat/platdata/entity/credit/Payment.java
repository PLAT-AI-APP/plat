package com.plat.platdata.entity.credit;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.credit.enums.PaymentStatus;
import com.plat.platdata.entity.credit.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency = "KRW";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "payment_at")
    private LocalDateTime paymentAt;

    @Column(name = "refund_at")
    private LocalDateTime refundAt;

    @Column(name = "refund_reason", columnDefinition = "TEXT")
    private String refundReason;

    @Column(name = "refund_deadline")
    private LocalDateTime refundDeadline;

    @Builder
    public Payment(Long userId, PaymentType paymentType, Long subscriptionId, String paymentMethod,
                   BigDecimal amount, String currency, PaymentStatus status) {
        this.userId = userId;
        this.paymentType = paymentType;
        this.subscriptionId = subscriptionId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.currency = currency != null ? currency : "KRW";
        this.status = status != null ? status : PaymentStatus.PENDING;
    }

}
