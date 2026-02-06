package com.plat.platdata.repository;

import com.plat.platdata.entity.chat.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
}
