package com.plat.platdata.entity.character;

import com.plat.platdata.entity.character.enums.Language;
import com.plat.platdata.entity.character.hashtag.Tag;
import com.plat.platdata.entity.character.lorebook.LorebookTranslation;
import com.plat.platdata.entity.character.scenario.ScenarioTranslation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterLanguagePack {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_lang_pack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    @Enumerated(EnumType.STRING)
    private Language language;
    private boolean isActive;

    // 1:1 관계 (이름, 소개 등)
    @OneToOne(mappedBy = "languagePack", cascade = CascadeType.ALL)
    private CharacterTranslation translation;

    // 시나리오 번역들
    @OneToMany(mappedBy = "languagePack", cascade = CascadeType.ALL)
    private List<ScenarioTranslation> scenarioTranslations = new ArrayList<>();

    // 로어북 번역들
    @OneToMany(mappedBy = "languagePack", cascade = CascadeType.ALL)
    private List<LorebookTranslation> lorebookTranslations = new ArrayList<>();

    // 이 언어 팩에 달린 태그들
    @OneToMany(mappedBy = "languagePack", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public CharacterLanguagePack(CharacterEntity character, Language language, boolean isActive) {
        this.character = character;
        this.language = language;
        this.isActive = isActive;
    }
}
