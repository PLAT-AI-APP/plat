package com.plat.platboot.mail.service;

import com.plat.platboot.mail.dto.MailSendDto;
import org.springframework.stereotype.Service;

@Service
public interface MailSender {

    void sendAuth(String toAddress, String authCode);

    void sendText(MailSendDto mailSend);
    void sendHtml(MailSendDto mailSend);
}
