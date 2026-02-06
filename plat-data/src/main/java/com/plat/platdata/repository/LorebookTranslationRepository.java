package com.plat.platdata.repository;

import com.plat.platdata.entity.character.lorebook.LorebookTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LorebookTranslationRepository extends JpaRepository<LorebookTranslation, Long> {
}
