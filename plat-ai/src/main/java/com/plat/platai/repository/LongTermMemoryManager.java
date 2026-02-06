package com.plat.platai.repository;

import com.plat.platai.dto.LongTermMemory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LongTermMemoryManager {

    private final Map<String, LongTermMemory> memories = new ConcurrentHashMap<>();

    public Optional<LongTermMemory> getMemory(String sessionId) {
        return Optional.ofNullable(memories.get(sessionId));
    }

    public void saveOrUpdateMemory(String sessionId, String summary, int sourceMessageCount) {
        memories.compute(sessionId, (key, existing) -> {
            if (existing == null) {
                return LongTermMemory.builder()
                    .sessionId(sessionId)
                    .summary(summary)
                    .sourceMessageCount(sourceMessageCount)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            } else {
                existing.updateSummary(summary, sourceMessageCount);
                return existing;
            }
        });
    }

    public void clearMemory(String sessionId) {
        memories.remove(sessionId);
    }
}
