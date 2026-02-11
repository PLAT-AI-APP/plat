package com.plat.platdata.jparepository;

import com.plat.platdata.entity.character.lorebook.Lorebook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LorebookRepository extends JpaRepository<Lorebook, Long> {
}
