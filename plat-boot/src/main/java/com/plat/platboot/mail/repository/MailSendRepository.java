package com.plat.platboot.mail.repository;

import com.plat.platboot.mail.dto.MailSendDto;

public interface MailSendRepository {

    void sendTextEmail(MailSendDto mailSendDto);
    void sendHtmlEmail(MailSendDto mailSendDto);

}
