package com.plat.platai.util;

import com.plat.platai.dto.ChatMessage;

import java.util.List;

public final class TokenEstimator {

    private static final double CHARS_PER_TOKEN = 2.5;

    private TokenEstimator() {}

    public static int estimateTokens(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        return (int) Math.ceil(text.length() / CHARS_PER_TOKEN);
    }

    public static int estimateTokens(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return 0;
        }
        return messages.stream()
            .mapToInt(msg -> estimateTokens(msg.getContent()))
            .sum();
    }
}
