package com.plat.platdata.repository;

import com.plat.platdata.entity.chat.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
}
