package com.plat.platdata.entity.chat;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.user.User;
import com.plat.platdata.entity.user.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "persona")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Persona extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "content")
    private String content;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Persona(User user, String name, Gender gender, LocalDate birth,
                   String content, Boolean isDefault) {
        this.user = user;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
        this.content = content;
        this.isDefault = isDefault != null ? isDefault : false;
    }
}
