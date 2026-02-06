package com.plat.platai.service;

import com.plat.platai.ai.ChatModelClientHandler;
import com.plat.platai.dto.ChatMessage;
import com.plat.platai.dto.LongTermMemory;
import com.plat.platai.repository.ConversationHistoryManager;
import com.plat.platai.repository.LongTermMemoryManager;
import com.plat.platai.util.TokenEstimator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConversationSummarizer {

    private final ChatModelClientHandler clientHandler;
    private final ConversationHistoryManager historyManager;
    private final LongTermMemoryManager memoryManager;

    private static final int SUMMARIZATION_TRIGGER_TOKENS = 3000;
    private static final int PROTECTED_RECENT_MESSAGES = 6;

    public void summarizeIfNeeded(String sessionId) {
        List<ChatMessage> history = historyManager.getHistory(sessionId);
        int totalTokens = TokenEstimator.estimateTokens(history);

        if (totalTokens < SUMMARIZATION_TRIGGER_TOKENS) {
            return;
        }

        List<ChatMessage> summarizableMessages = historyManager.getMessagesForSummarization(sessionId, PROTECTED_RECENT_MESSAGES);
        if (summarizableMessages.isEmpty()) {
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                performSummarization(sessionId, summarizableMessages);
            } catch (Exception e) {
                log.error("대화 요약 실패 (세션: {}): ", sessionId, e);
            }
        });
    }

    private void performSummarization(String sessionId, List<ChatMessage> messagesToSummarize) {
        StringBuilder conversationText = new StringBuilder();
        for (ChatMessage msg : messagesToSummarize) {
            String roleLabel = msg.getRole() == ChatMessage.Role.USER ? "User" : "Assistant";
            conversationText.append(roleLabel).append(": ").append(msg.getContent()).append("\n");
        }

        Optional<LongTermMemory> existingMemory = memoryManager.getMemory(sessionId);

        String prompt;
        prompt = existingMemory.map(longTermMemory -> String.format(
            "기존 대화 요약:\n%s\n\n새로운 대화 내용:\n%s\n\n" +
                "위의 기존 요약과 새로운 대화를 통합하여 핵심 내용을 간결하게 요약해주세요. " +
                "중요한 사실, 감정, 관계 변화를 중심으로 300자 이내로 작성하세요.",
            longTermMemory.getSummary(),
            conversationText
        )).orElseGet(() -> String.format(
            "다음 대화 내용을 요약해주세요:\n%s\n\n" +
                "중요한 사실, 감정, 관계 변화를 중심으로 핵심 내용을 300자 이내로 간결하게 요약하세요.",
            conversationText
        ));

        String summary = clientHandler.getSummarizationClient()
            .call("당신은 대화 요약 전문가입니다. 주어진 대화의 핵심을 간결하게 요약합니다.", prompt);

        memoryManager.saveOrUpdateMemory(sessionId, summary, messagesToSummarize.size());
        historyManager.removeOldestMessages(sessionId, messagesToSummarize.size());

        log.info("세션 {} 대화 요약 완료: {}개 메시지 요약됨", sessionId, messagesToSummarize.size());
    }
}
