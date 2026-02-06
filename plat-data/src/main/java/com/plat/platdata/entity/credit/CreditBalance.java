package com.plat.platdata.entity.credit;

import com.plat.platdata.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_balance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditBalance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "balance_id")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount = 0;

    @Column(name = "used_amount", nullable = false)
    private Integer usedAmount = 0;

    @Column(name = "remaining_amount", nullable = false)
    private Integer remainingAmount = 0;


    @Builder
    public CreditBalance(Long userId, Integer totalAmount, Integer usedAmount, Integer remainingAmount) {
        this.userId = userId;
        this.totalAmount = totalAmount != null ? totalAmount : 0;
        this.usedAmount = usedAmount != null ? usedAmount : 0;
        this.remainingAmount = remainingAmount != null ? remainingAmount : 0;
    }

}
