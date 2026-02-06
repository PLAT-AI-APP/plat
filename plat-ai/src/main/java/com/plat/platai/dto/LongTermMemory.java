package com.plat.platai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LongTermMemory {

    private String sessionId;
    private String summary;
    private int sourceMessageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updateSummary(String newSummary, int additionalMessages) {
        this.summary = newSummary;
        this.sourceMessageCount += additionalMessages;
        this.updatedAt = LocalDateTime.now();
    }
}
