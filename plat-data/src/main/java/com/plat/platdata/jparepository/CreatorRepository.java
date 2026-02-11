package com.plat.platdata.jparepository;

import com.plat.platdata.entity.character.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
}
