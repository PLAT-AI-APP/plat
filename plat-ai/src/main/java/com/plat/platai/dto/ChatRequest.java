package com.plat.platai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String sessionId;   // 채팅방 ID
    private String message;     // 사용자 메시지
    private Long persona;
    private ChatModel model;   // AI 모델 (선택사항)
}