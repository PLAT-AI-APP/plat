package com.plat.platboot.mail.exceptions;

public class SMTPBadCredentialsException extends EmailInternalException {

    public SMTPBadCredentialsException() {
        super("SMTP 아이디 또는 비밀번호가 일치하지 않습니다.");
    }
}
