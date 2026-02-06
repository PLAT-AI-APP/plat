package com.plat.platdata.entity.user;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.enums.Language;
import com.plat.platdata.entity.character.Creator;
import com.plat.platdata.entity.chat.Persona;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "birth", nullable = false)
    private LocalDateTime birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "adult_verified", nullable = false)
    private Boolean adultVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_language", nullable = false)
    private Language preferredLanguage = Language.KO;


    // TODO OneToOne EAGER 되는건 실제 로직을 작성해보고 단방향 매핑으로도 충분하면 이후 단방향으로 변경예정, 양방향 필요하면 List로 속여서 가져오게 하자
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Creator creator;

    // 인증 정보 (이메일, 카카오, 구글 등 다중 연동 가능하므로 1:N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Authentication> authentications = new ArrayList<>();

    // 본인 인증 이력 (1:N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Verification> verifications = new ArrayList<>();

    // 페르소나 (양방향으로 변경하여 관리 효율성 증대)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Persona> personas = new ArrayList<>();

    // 토큰 (1:N)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

    @Builder
    public User(String nickname, String profileImage, LocalDateTime birth, String gender,
                String phone, Boolean adultVerified, Language preferredLanguage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.birth = birth;
        this.gender = gender;
        this.phone = phone;
        this.adultVerified = adultVerified != null ? adultVerified : false;
        this.preferredLanguage = preferredLanguage != null ? preferredLanguage : Language.KO;
    }

    public boolean isCreator() {
        return this.creator != null;
    }

}
