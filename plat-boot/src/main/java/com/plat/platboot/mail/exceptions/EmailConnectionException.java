package com.plat.platboot.mail.exceptions;

import lombok.Getter;

@Getter
public class EmailConnectionException extends EmailException {

    private final String host;
    private final int port;

    public EmailConnectionException(String host, int port) {
        super("SMTP 서버 주소 또는 포트가 올바르지 않습니다.");
        this.host = host;
        this.port = port;
    }
}
