package com.plat.platboot.mail.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailSendDto {

    /**
     * address : 보낼 주소
     * subject : 메일 제목
     * content : 메일 내용
     */

    private final String[] address;
    private final String subject;
    private final String content;

    public static MailBuilder to(String... addresses) {
        return new MailBuilder(addresses);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MailBuilder {
        private final String[] addresses;

        public MailSendDto write(String subject, String content) {
            return new MailSendDto(addresses, subject, content);
        }

    }
}
