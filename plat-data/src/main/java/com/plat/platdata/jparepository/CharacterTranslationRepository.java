package com.plat.platdata.jparepository;

import com.plat.platdata.entity.character.CharacterTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterTranslationRepository extends JpaRepository<CharacterTranslation, Long> {
}
