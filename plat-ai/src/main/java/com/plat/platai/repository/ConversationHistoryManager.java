package com.plat.platai.repository;

import com.plat.platai.dto.ChatMessage;
import com.plat.platai.util.TokenEstimator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConversationHistoryManager {

    private final Map<String, List<ChatMessage>> histories = new ConcurrentHashMap<>();

    public static final int HISTORY_TOKEN_BUDGET = 4000;
    public static final int PROTECTED_RECENT_MESSAGES = 6;

    public void createSession(String sessionId) {
        histories.put(sessionId, new ArrayList<>());
    }

    public void addMessage(String sessionId, ChatMessage message) {
        List<ChatMessage> history = histories.computeIfAbsent(sessionId, k -> new ArrayList<>());
        history.add(message);
    }

    public List<ChatMessage> getHistory(String sessionId) {
        return new ArrayList<>(histories.getOrDefault(sessionId, new ArrayList<>()));
    }

    public void clearHistory(String sessionId) {
        histories.remove(sessionId);
    }

    /**
     * 토큰 버짓 내에서 최신 메시지부터 역순으로 반환
     */
    public List<ChatMessage> getMessagesWithinBudget(String sessionId, int tokenBudget) {
        List<ChatMessage> history = histories.getOrDefault(sessionId, new ArrayList<>());
        List<ChatMessage> result = new ArrayList<>();
        int usedTokens = 0;

        for (int i = history.size() - 1; i >= 0; i--) {
            ChatMessage msg = history.get(i);
            int msgTokens = TokenEstimator.estimateTokens(msg.getContent());
            if (usedTokens + msgTokens > tokenBudget && !result.isEmpty()) {
                break;
            }
            result.addFirst(msg);
            usedTokens += msgTokens;
        }

        return result;
    }

    /**
     * 요약 대상 메시지 반환 (최근 protectedCount개를 제외한 나머지)
     */
    public List<ChatMessage> getMessagesForSummarization(String sessionId, int protectedCount) {
        List<ChatMessage> history = histories.getOrDefault(sessionId, new ArrayList<>());
        int size = history.size();
        if (size <= protectedCount) {
            return new ArrayList<>();
        }
        return new ArrayList<>(history.subList(0, size - protectedCount));
    }

    /**
     * 가장 오래된 메시지 N개 제거
     */
    public void removeOldestMessages(String sessionId, int count) {
        List<ChatMessage> history = histories.get(sessionId);
        if (history == null || history.isEmpty()) {
            return;
        }
        int removeCount = Math.min(count, history.size());
        history.subList(0, removeCount).clear();
    }

    public List<ChatMessage> getRecentMessages(String sessionId, int count) {
        List<ChatMessage> history = getHistory(sessionId);
        int size = history.size();
        int fromIndex = Math.max(0, size - count);
        return new ArrayList<>(history.subList(fromIndex, size));
    }

    public String formatHistoryAsContext(String sessionId, int recentCount) {
        List<ChatMessage> recentMessages = getRecentMessages(sessionId, recentCount);
        StringBuilder context = new StringBuilder();

        for (ChatMessage message : recentMessages) {
            String roleLabel = message.getRole() == ChatMessage.Role.USER ? "User" : "Assistant";
            context.append(roleLabel).append(": ").append(message.getContent()).append("\n");
        }

        return context.toString();
    }
}
