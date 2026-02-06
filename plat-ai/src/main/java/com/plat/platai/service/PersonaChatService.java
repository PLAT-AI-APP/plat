package com.plat.platai.service;

import com.plat.platai.ai.ChatModelClientHandler;
import com.plat.platai.dto.ChatMessage;
import com.plat.platai.dto.ChatModel;
import com.plat.platai.dto.LongTermMemory;
import com.plat.platai.entity.Persona;
import com.plat.platai.entity.PersonaLorebook;
import com.plat.platai.exception.ChatException;
import com.plat.platai.repository.ConversationHistoryManager;
import com.plat.platai.repository.LongTermMemoryManager;
import com.plat.platai.util.MessageConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonaChatService {

    private final ChatModelClientHandler clientHandler;
    private final PersonaPromptBuilder promptBuilder;
    private final ConversationHistoryManager historyManager;
    private final LongTermMemoryManager memoryManager;
    private final ConversationSummarizer summarizer;
    private final MessageSource messageSource;

    public Flux<String> chat(String sessionId, String userMessage, Persona persona, ChatModel model) {
        // 1. 사용자 메시지를 히스토리에 추가
        ChatMessage userMsg = ChatMessage.user(userMessage);
        historyManager.addMessage(sessionId, userMsg);

        // 2. 토큰 버짓 내 대화 히스토리 조회
        List<ChatMessage> budgetMessages = historyManager.getMessagesWithinBudget(
            sessionId, ConversationHistoryManager.HISTORY_TOKEN_BUDGET);

        // 3. Spring AI Message로 변환 (멀티턴)
        List<Message> springAiMessages = MessageConverter.toSpringAiMessages(budgetMessages);

        // 4. 장기기억 조회
        LongTermMemory longTermMemory = memoryManager.getMemory(sessionId).orElse(null);

        // 5. 최근 대화 컨텍스트로 로어북 필터링
        String recentContext = buildRecentContext(budgetMessages);
        List<PersonaLorebook> activeLorebook = promptBuilder.filterActiveLorebook(persona, recentContext);

        // 6. 시스템 프롬프트 조합 (Layer 1~3)
        String systemPrompt = promptBuilder.buildSystemPrompt(persona, null, longTermMemory, activeLorebook);

        log.info("=== 페르소나 채팅 시작 ===");
        log.info("세션 ID: {}", sessionId);
        log.info("모델: {}", model);
        log.info("사용자 메시지: {}", userMessage);
        log.info("대화 히스토리: {}개 메시지", budgetMessages.size());
        log.info("활성 로어북: {}개", activeLorebook.size());
        log.info("장기기억: {}", longTermMemory != null ? "있음" : "없음");
        log.info("시스템 프롬프트:\n{}", systemPrompt);

        // 7. 멀티턴 스트리밍
        Flux<String> stream = clientHandler.get(model)
            .stream(systemPrompt, springAiMessages);

        // 8. 응답 완료 후 히스토리 저장 + 비동기 요약
        StringBuilder fullResponse = new StringBuilder();
        return stream
            .doOnNext(chunk -> {
                fullResponse.append(chunk);
                log.trace("응답 청크: {}", chunk);
            })
            .doOnComplete(() -> {
                String completeResponse = fullResponse.toString();
                ChatMessage assistantMsg = ChatMessage.assistant(completeResponse);
                historyManager.addMessage(sessionId, assistantMsg);
                log.info("완료된 응답: {}", completeResponse);
                log.info("=== 페르소나 채팅 종료 ===");

                // 비동기 요약 트리거
                summarizer.summarizeIfNeeded(sessionId);
            })
            .doOnError(error -> {
                log.error("채팅 오류 발생: ", error);
            })
            .onErrorResume(error -> {
                ChatException chatException = convertToChatException(error);
                log.error("사용자에게 표시할 메시지: {}", chatException.getUserFriendlyMessage());
                return Flux.just(chatException.getUserFriendlyMessage());
            });
    }

    private String buildRecentContext(List<ChatMessage> messages) {
        StringBuilder sb = new StringBuilder();
        int limit = Math.min(messages.size(), 4);
        for (int i = messages.size() - limit; i < messages.size(); i++) {
            sb.append(messages.get(i).getContent()).append(" ");
        }
        return sb.toString();
    }

    private ChatException convertToChatException(Throwable throwable) {
        if (throwable instanceof ChatException error) {
            return error;
        }

        String errorMsg = throwable.getMessage() != null ? throwable.getMessage().toLowerCase() : "";

        Locale locale = LocaleContextHolder.getLocale();
        if (errorMsg.contains("api key") || errorMsg.contains("authentication") || errorMsg.contains("unauthorized")) {
            return new ChatException("API 인증 실패: " + throwable.getMessage(), messageSource.getMessage("authentication", null, locale), throwable);
        } else if (errorMsg.contains("rate limit") || errorMsg.contains("quota")) {
            return new ChatException("Rate limit 초과: " + throwable.getMessage(), messageSource.getMessage("ratelimit", null, locale), throwable);
        } else if (errorMsg.contains("timeout")) {
            return new ChatException("API 응답 시간 초과: " + throwable.getMessage(), messageSource.getMessage("timeout", null, locale), throwable);
        } else if (errorMsg.contains("overloaded") || errorMsg.contains("capacity")) {
            return new ChatException("서버 과부하: " + throwable.getMessage(), messageSource.getMessage("serverOverloaded", null, locale), throwable);
        } else {
            return new ChatException("예상치 못한 오류: " + throwable.getMessage(), "죄송합니다. 일시적인 오류가 발생했습니다. 다시 시도해주세요.", throwable) {};
        }
    }

    public String getConversationHistory(String sessionId) {
        return historyManager.formatHistoryAsContext(sessionId, 20);
    }

    public void clearConversation(String sessionId) {
        historyManager.clearHistory(sessionId);
        memoryManager.clearMemory(sessionId);
        log.info("세션 {} 대화 히스토리 및 장기기억 초기화 완료", sessionId);
    }

    public void startNewSession(String sessionId) {
        historyManager.createSession(sessionId);
        log.info("새 세션 {} 생성 완료", sessionId);
    }
}
