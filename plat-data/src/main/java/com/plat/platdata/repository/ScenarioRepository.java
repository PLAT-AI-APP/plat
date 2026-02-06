package com.plat.platdata.repository;

import com.plat.platdata.entity.character.scenario.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
}
