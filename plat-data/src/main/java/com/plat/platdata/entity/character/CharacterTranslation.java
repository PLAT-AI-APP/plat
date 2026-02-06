package com.plat.platdata.entity.character;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "character_translation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterTranslation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_translation_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_lang_pack_id")
    private CharacterLanguagePack languagePack;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "introduction", nullable = false, length = 500)
    private String introduction;

    @Column(name = "detail_info", length = 300)
    private String detailInfo;

    @Column(name = "detail_setting", nullable = false, columnDefinition = "TEXT")
    private String detailSetting;

    @Column(name = "is_original", nullable = false)
    private Boolean isOriginal = false;

    @Column(name = "jobs")
    private String jobs;

    @Column(name = "interests")
    private String interests;

    @Column(name = "likes")
    private String likes;

    @Column(name = "dislikes")
    private String dislikes;

    @Builder
    public CharacterTranslation(CharacterLanguagePack languagePack, String name, String introduction,
                                String detailInfo, String detailSetting, Boolean isOriginal,
                                List<String> jobs, List<String> interests, List<String> likes, List<String> dislikes) {
        this.languagePack = languagePack;
        this.name = name;
        this.introduction = introduction;
        this.detailInfo = detailInfo;
        this.detailSetting = detailSetting;
        this.isOriginal = isOriginal != null ? isOriginal : false;
        this.jobs = jobs.toString();
        this.interests = interests.toString();
        this.likes = likes.toString();
        this.dislikes = dislikes.toString();
    }

}
