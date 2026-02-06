package com.plat.platai.controller;


import com.plat.platai.dto.ChatRequest;
import com.plat.platai.entity.Persona;
import com.plat.platai.service.PersonaChatService;
import com.plat.platai.service.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class PersonaChatController {

    private final PersonaService personaService;
    private final PersonaChatService chatService;

    /**
     * 페르소나 채팅 - 스트림 방식
     *
     * SSE를 통해 실시간으로 응답을 받을 수 있습니다.
     *
     * @param request 채팅 요청 (sessionId, message, modelName)
     * @return 스트림 응답
     */
    @PostMapping(value = "/stream",
        produces = "text/plain; charset=UTF-8"
    )
    public Flux<String> chatStream(@RequestBody ChatRequest request) {

        log.info("=== 요청 수신 ===");
        log.info("SessionId: {}", request.getSessionId());
        log.info("Message: {}", request.getMessage());
        log.info("Model (raw): {}", request.getModel());
        log.info("Model (toString): {}", request.getModel() != null ? request.getModel().toString() : "null");

        // 페르소나 컨텍스트 생성 (실제로는 DB에서 조회하거나 요청에서 받아야 함)
        // 여기서는 예시 페르소나 사용
        Persona persona = personaService.getById(request.getPersona());

        return chatService.chat(
            request.getSessionId(),
            request.getMessage(),
            persona,
            request.getModel()
        );
    }

    /**
     * 일반 채팅 - 전체 응답 한번에
     *
     * 스트림이 아닌 완성된 응답을 한번에 받습니다.
     *
     * @param request 채팅 요청
     * @return 완성된 응답
     */
    @PostMapping("/message")
    public Flux<String> chatMessage(@RequestBody ChatRequest request) {

        Persona persona = personaService.getById(request.getPersona());

        return chatService.chat(
                request.getSessionId(),
                request.getMessage(),
                persona,
                request.getModel()
            ).collectList()
            .map(chunks -> String.join("", chunks))
            .flux();
    }

    /**
     * 새 세션 시작
     */
    @PostMapping("/session/{sessionId}/start")
    public Map<String, String> startSession(@PathVariable String sessionId) {
        chatService.startNewSession(sessionId);
        return Map.of("message", "세션이 시작되었습니다", "sessionId", sessionId);
    }

    /**
     * 세션 대화 히스토리 조회
     */
    @GetMapping("/session/{sessionId}/history")
    public Map<String, String> getHistory(@PathVariable String sessionId) {
        String history = chatService.getConversationHistory(sessionId);
        return Map.of("sessionId", sessionId, "history", history);
    }

    /**
     * 세션 대화 히스토리 초기화
     */
    @DeleteMapping("/session/{sessionId}")
    public Map<String, String> clearSession(@PathVariable String sessionId) {
        chatService.clearConversation(sessionId);
        return Map.of("message", "대화 히스토리가 초기화되었습니다", "sessionId", sessionId);
    }

    /**
     * 헬스체크
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "OK", "service", "PersonaChatService");
    }
}