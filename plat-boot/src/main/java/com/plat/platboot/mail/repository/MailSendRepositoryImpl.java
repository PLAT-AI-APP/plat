package com.plat.platboot.mail.repository;

import com.plat.platboot.mail.component.MailSendExceptionHandlerHelper;
import com.plat.platboot.mail.dto.MailSendDto;
import com.plat.platboot.mail.exceptions.EmailInternalException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Slf4j
@Repository
@RequiredArgsConstructor
class MailSendRepositoryImpl implements MailSendRepository {

    private final JavaMailSender mailSender;
    private final MailSendExceptionHandlerHelper templateHelper;

    @Override
    public void sendTextEmail(MailSendDto mailSendDto) {
        var message = generateTextMessage(mailSendDto);
        templateHelper.mailSendTemplate(() -> mailSender.send(message));
        logSuccess(mailSendDto);
    }

    @Override
    public void sendHtmlEmail(MailSendDto mailSendDto) {
        var message = generateHTMLMessage(mailSendDto);
        templateHelper.mailSendTemplate(() -> mailSender.send(message));
        logSuccess(mailSendDto);
    }

    private void logSuccess(MailSendDto mailSendDto) {
        log.info("[{}] 템플릿 이메일 전송 성공", Arrays.toString(mailSendDto.getAddress()));
    }

    private SimpleMailMessage generateTextMessage(MailSendDto mailDto) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(mailDto.getAddress());                // 받는 사람 이메일
        smm.setSubject(mailDto.getSubject());            // 이메일 제목
        smm.setText(mailDto.getContent());               // 이메일 내용
        return smm;
    }

    private MimeMessage generateHTMLMessage(MailSendDto mailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(mailDto.getAddress());
            helper.setSubject(mailDto.getSubject());
            helper.setText(mailDto.getContent(), true);
            return message;
        } catch (MessagingException e) {
            throw new EmailInternalException("MimeMessage 생성 실패 : " + e.getMessage());
        }
    }


}
