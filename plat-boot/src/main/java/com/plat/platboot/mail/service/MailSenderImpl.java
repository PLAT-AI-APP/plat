package com.plat.platboot.mail.service;

import com.plat.platboot.mail.dto.MailSendDto;
import com.plat.platboot.mail.enums.EmailTemplate;
import com.plat.platboot.mail.repository.MailSendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.plat.platboot.mail.enums.EmailTemplate.*;


@Service
@RequiredArgsConstructor
class MailSenderImpl implements MailSender {

    private final MailSendRepository mailSendRepository;

    @Override
    public void sendAuth(final String toAddress, final String authCode) {

        String htmlContent = AUTH.toHTMLString(Map.of("authKey", authCode));

        MailSendDto mail = MailSendDto
            .to(toAddress)
            .write("[PLAT] 이메일 인증메일입니다.", htmlContent);

        mailSendRepository.sendHtmlEmail(mail);
    }

    @Override
    public void sendText(MailSendDto mailSend) {
        mailSendRepository.sendTextEmail(mailSend);
    }

    @Override
    public void sendHtml(MailSendDto mailSend) {
        mailSendRepository.sendHtmlEmail(mailSend);
    }
}