package com.plat.platboot.mail.exceptions;


/**
 * 이메일 외부 예외 : 사용자에 의한 예외
 */
public class EmailExternalException extends EmailException {

    public EmailExternalException() {
        super("메일 서버에 문제가 발생했습니다.");
    }

    public EmailExternalException(String message) {
        super(message);
    }

}
