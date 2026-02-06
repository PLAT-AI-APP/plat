package com.plat.platdata.entity.user;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.user.enums.VerificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "veri_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Long userId에서 변경

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type", nullable = false)
    private VerificationType verificationType;

    @Column(name = "verification_method", nullable = false, length = 50)
    private String verificationMethod;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = true;

    @Column(name = "verified_at", nullable = false)
    private LocalDateTime verifiedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "verification_data", columnDefinition = "JSON")
    private String verificationData;

    @Column(name = "ci", length = 255)
    private String ci;

    @Column(name = "di", length = 255)
    private String di;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "revoked_reason")
    private String revokedReason;

    @Builder
    public Verification(User user, VerificationType verificationType, String verificationMethod,
                        Boolean isVerified, LocalDateTime verifiedAt, LocalDateTime expiresAt,
                        String verificationData, String ci, String di,
                        String ipAddress, String userAgent) {
        this.user = user;
        this.verificationType = verificationType;
        this.verificationMethod = verificationMethod;
        this.isVerified = isVerified != null ? isVerified : true;
        this.verifiedAt = verifiedAt;
        this.expiresAt = expiresAt;
        this.verificationData = verificationData;
        this.ci = ci;
        this.di = di;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

}
