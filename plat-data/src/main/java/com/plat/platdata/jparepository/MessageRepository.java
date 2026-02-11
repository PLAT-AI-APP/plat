package com.plat.platdata.jparepository;

import com.plat.platdata.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
