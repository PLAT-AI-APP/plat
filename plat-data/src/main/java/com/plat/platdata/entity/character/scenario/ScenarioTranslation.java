package com.plat.platdata.entity.character.scenario;

import com.plat.platdata.entity.character.CharacterLanguagePack;
import com.plat.platdata.entity.character.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scenario_translation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScenarioTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_translation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_lang_pack_id")
    private CharacterLanguagePack languagePack;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "location", nullable = false, length = 20)
    private String location;

    @Column(name = "first_situation", nullable = false, length = 1000)
    private String firstSituation;

    @Column(name = "first_dialogue", nullable = false, length = 500)
    private String firstDialogue;

    @Builder
    public ScenarioTranslation(Scenario scenario, CharacterLanguagePack languagePack, String title, String location,
                               String firstSituation, String firstDialogue) {
        this.scenario = scenario;
        this.languagePack = languagePack;
        this.title = title;
        this.location = location;
        this.firstSituation = firstSituation;
        this.firstDialogue = firstDialogue;
    }
}
