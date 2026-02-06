package com.plat.platdata.entity.character.scenario;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.CharacterEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scenario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scenario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private CharacterEntity character;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @OneToMany(mappedBy = "scenario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScenarioTranslation> scenarioTranslations = new ArrayList<>();

    @Builder
    public Scenario(CharacterEntity character, int displayOrder, List<ScenarioTranslation> scenarioTranslations) {
        this.character = character;
        this.displayOrder = displayOrder;
        this.scenarioTranslations = scenarioTranslations != null ? scenarioTranslations : new ArrayList<>();
    }

}
