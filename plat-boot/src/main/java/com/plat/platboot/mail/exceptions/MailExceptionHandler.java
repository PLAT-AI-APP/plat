package com.plat.platboot.mail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class MailExceptionHandler {

    @ExceptionHandler(EmailExternalException.class)
    public ResponseEntity<ErrorResponse> handleMailExternalException(EmailExternalException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            LocalDateTime.now());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(EmailInternalException.class)
    public ResponseEntity<ErrorResponse> handleMailInternalException(EmailInternalException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            e.getMessage(),
            LocalDateTime.now());
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    /**
     * API 에러 응답 DTO
     *
     * @param status    HTTP 상태 코드
     * @param message   에러 메시지
     * @param timestamp 발생 시각
     */
    public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp
    ) {
    }

    /**
     * 검증 에러 응답 DTO
     *
     * @param status    HTTP 상태 코드
     * @param message   에러 메시지
     * @param timestamp 발생 시각
     * @param errors    필드별 오류 메시지 맵
     */
    public record ValidationErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> errors
    ) {
    }
}
