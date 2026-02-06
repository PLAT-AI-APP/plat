package com.plat.platdata.entity.user;

import com.plat.platdata.entity.user.enums.AuthType;
import com.plat.platdata.entity.user.enums.Provider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "authentication")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Builder
    public Authentication(User user, AuthType authType, Provider provider, String providerId,
                          String email, String password) {
        this.user = user;
        this.authType = authType;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.password = password;
    }
}
