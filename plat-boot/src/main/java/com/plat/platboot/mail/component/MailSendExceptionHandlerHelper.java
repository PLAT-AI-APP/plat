package com.plat.platboot.mail.component;

import com.plat.platboot.mail.exceptions.*;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.SendFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class MailSendExceptionHandlerHelper {

    public void mailSendTemplate(Runnable runnable) {
        try {
            runnable.run();
        } catch (MailAuthenticationException e) {
            log.error("SMTP 인증 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
            throw new SMTPBadCredentialsException();
        } catch (MailSendException ex) {
            handleMailSendException(ex);
        }
    }

    private void handleMailSendException(MailSendException ex) {
        Exception[] messageExceptions = ex.getMessageExceptions();

        if (messageExceptions == null || messageExceptions.length == 0) {
            log.error("메일 전송 실패: {}", ex.getMessage());
            throw new EmailInternalException();
        }

        for (Exception e : messageExceptions) {
            if (e instanceof SendFailedException s) {
                handleSendFailedException(s);
            } else if (isConnectionException(e)) {
                handleConnectionException(e);
            } else if (e instanceof MessagingException s) {
                handleMessagingException(s);
            } else {
                log.warn("처리되지 않은 예외 타입: {}", e.getClass().getName(), e);
            }
        }

        throw new EmailInternalException();
    }

    private void handleSendFailedException(SendFailedException e) {
        // Invalid addresses (잘못된 이메일 형식)
        Address[] invalidAddresses = e.getInvalidAddresses();
        if (invalidAddresses != null && invalidAddresses.length > 0) {
            List<String> emails = Arrays.stream(invalidAddresses)
                .map(Address::toString)
                .toList();
            log.error("잘못된 이메일 주소: {}", emails);
            throw new InvalidEmailException(emails);
        }

        // Valid unsent addresses (전송되지 않은 유효한 주소들)
        Address[] validUnsentAddresses = e.getValidUnsentAddresses();
        if (validUnsentAddresses != null && validUnsentAddresses.length > 0) {
            log.error("전송 실패한 유효한 주소들: {}",
                Arrays.toString(validUnsentAddresses));
            throw new EmailExternalException("일부 수신자에게 메일 전송 실패");
        }

        // 기타 SendFailedException
        log.error("메일 전송 실패: {}", e.getMessage(), e);
        throw new EmailExternalException("메일 전송 중 오류 발생");
    }

    private boolean isConnectionException(Exception e) {
        // 연결 관련 예외 판단
        return e instanceof ConnectException
            || e.getCause() instanceof ConnectException
            || e.getMessage() != null && (
            e.getMessage().contains("Connection")
                || e.getMessage().contains("connect")
                || e.getMessage().contains("host")
        );
    }

    private void handleConnectionException(Exception e) {
        String message = e.getMessage();
        log.error("SMTP 서버 연결 실패: {}", message);

        // 메시지에서 호스트와 포트 추출 시도 (선택적)
        String host = extractHost(message);
        Integer port = extractPort(message);

        throw new EmailConnectionException(host, port);
    }

    private void handleMessagingException(MessagingException e) {
        String message = e.getMessage();

        // 인증 관련 메시지 체크
        if (message != null && (message.contains("authentication")
            || message.contains("Authentication")
            || message.contains("credentials"))) {
            log.error("SMTP 인증 오류: {}", message);
            throw new SMTPBadCredentialsException();
        }

        log.error("메시징 예외 발생: {}", message, e);
        throw new EmailInternalException();
    }

    private String extractHost(String message) {
        if (message == null) return "unknown";

        // "host: smtp.gmail.com" 같은 패턴에서 추출
        if (message.contains("host")) {
            String[] parts = message.split("host[:\\s]+");
            if (parts.length > 1) {
                return parts[1].split("[,;\\s]")[0];
            }
        }
        return "unknown";
    }

    private Integer extractPort(String message) {
        if (message == null) return null;

        // "port: 587" 같은 패턴에서 추출
        if (message.contains("port")) {
            String[] parts = message.split("port[:\\s]+");
            if (parts.length > 1) {
                try {
                    return Integer.parseInt(parts[1].split("[,;\\s]")[0]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return null;
    }
}