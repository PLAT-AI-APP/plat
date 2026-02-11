package com.plat.platdata.jparepository;

import com.plat.platdata.entity.character.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterEntityRepository extends JpaRepository<CharacterEntity, Long> {
}
