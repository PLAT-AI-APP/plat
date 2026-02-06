package com.plat.platdata.entity.credit;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.credit.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    @Builder
    public CreditTransaction(Long userId, TransactionType transactionType, String description,
                             Integer amount, Integer balanceAfter) {
        this.userId = userId;
        this.transactionType = transactionType;
        this.description = description;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }
}
