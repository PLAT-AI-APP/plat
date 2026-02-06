package com.plat.platai.exception;

import lombok.Getter;

/**
 * 채팅 관련 예외의 기본 클래스
 * 모든 채팅 예외는 사용자 친화적인 메시지를 포함
 */
@Getter
public class ChatException extends RuntimeException {

    private final String userFriendlyMessage;

    public ChatException(String technicalMessage, String userFriendlyMessage) {
        super(technicalMessage);
        this.userFriendlyMessage = userFriendlyMessage;
    }

    public ChatException(String technicalMessage, String userFriendlyMessage, Throwable cause) {
        super(technicalMessage, cause);
        this.userFriendlyMessage = userFriendlyMessage;
    }
}