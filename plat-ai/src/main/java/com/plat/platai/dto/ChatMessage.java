package com.plat.platai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 대화 메시지를 표현하는 클래스
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    /**
     * 메시지 역할 (USER, ASSISTANT, SYSTEM)
     */
    public enum Role {
        USER,       // 사용자 메시지
        ASSISTANT,  // AI 응답
        SYSTEM      // 시스템 메시지 (프롬프트)
    }

    private Role role;              // 메시지 역할
    private String content;         // 메시지 내용
    private LocalDateTime timestamp; // 전송 시간

    /**
     * 사용자 메시지 생성
     */
    public static ChatMessage user(String content) {
        return build(Role.USER, content);
    }

    /**
     * AI 응답 메시지 생성
     */
    public static ChatMessage assistant(String content) {
        return build(Role.ASSISTANT, content);
    }

    /**
     * 시스템 메시지 생성
     */
    public static ChatMessage system(String content) {
        return build(Role.SYSTEM, content);
    }

    private static ChatMessage build(Role role, String content) {
        return ChatMessage.builder()
            .role(role)
            .content(content)
            .timestamp(LocalDateTime.now())
            .build();
    }
}