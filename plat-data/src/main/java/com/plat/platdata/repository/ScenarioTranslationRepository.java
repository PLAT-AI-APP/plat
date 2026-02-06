package com.plat.platdata.repository;

import com.plat.platdata.entity.character.scenario.ScenarioTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioTranslationRepository extends JpaRepository<ScenarioTranslation, Long> {
}
