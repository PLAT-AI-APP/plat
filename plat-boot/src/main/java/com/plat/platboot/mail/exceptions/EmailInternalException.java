package com.plat.platboot.mail.exceptions;


/**
 * 이메일 서버 예외 : 서버 내부 설정에 의한 예외
 */
public class EmailInternalException extends EmailException {

    public EmailInternalException() {
        super("메일 전송에 실패했습니다.");
    }

    public EmailInternalException(String message) {
        super(message);
    }
}
