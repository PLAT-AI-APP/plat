package com.plat.platdata.entity.credit;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.credit.enums.CreditSourceType;
import com.plat.platdata.entity.credit.enums.CreditStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private CreditSourceType sourceType;

    @Column(name = "original_amount", nullable = false)
    private Integer originalAmount;

    @Column(name = "remaining_amount", nullable = false)
    private Integer remainingAmount;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CreditStatus status = CreditStatus.ACTIVE;

    @Builder
    public Credit(Long userId, Long paymentId, CreditSourceType sourceType,
                  Integer originalAmount, Integer remainingAmount,
                  LocalDateTime issuedAt, LocalDateTime expiresAt, CreditStatus status) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.sourceType = sourceType;
        this.originalAmount = originalAmount;
        this.remainingAmount = remainingAmount;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.status = status != null ? status : CreditStatus.ACTIVE;
    }
}
