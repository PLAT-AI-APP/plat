package com.plat.platboot.auth.controller;

import com.plat.platboot.auth.dto.*;
import com.plat.platboot.auth.service.AuthService;
import com.plat.platboot.auth.service.EmailVerificationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    // 이메일 인증 요청
    @PostMapping("/signup/email/verify")
    public ResponseEntity<?> requestEmailVerification(@Validated @RequestBody EmailVerifyRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "",
                    (existing, _) -> existing));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }
        emailVerificationService.sendVerificationCode(request.email());
        return ResponseEntity.ok(Map.of("message", "authCode.send"));
    }

    // 이메일 인증 확인 (스켈레톤)
    @PostMapping("/signup/email/verify/confirm")
    public ResponseEntity<?> confirmEmailVerification(@Validated @RequestBody EmailVerifyConfirmRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "",
                    (existing, _) -> existing));
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        boolean verified = emailVerificationService.verifyCode(request.email(), request.code());
        if (verified) {
            return ResponseEntity.ok(Map.of("verified", "authCode.success"));
        } else {
            return ResponseEntity.ok(Map.of("verified", "authCode.fail"));
        }
    }

    // 이메일 가입 1단계 → signupToken 반환
    @PostMapping("/signup/email")
    public ResponseEntity<SignupTokenResponse> emailSignup(
            @Valid @RequestBody EmailSignupRequest request) {
        if (!request.password().equals(request.passwordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        SignupTokenResponse signupTokenResponse = authService.initiateEmailSignup(
            request.email(), request.password());
        return ResponseEntity.ok(signupTokenResponse);
    }

    // 가입 2단계 (이메일/소셜 공통) → User 생성 + JWT 발급
    @PostMapping("/signup/complete")
    public ResponseEntity<Map<String, String>> completeSignup(
            @Valid @RequestBody SignupCompleteRequest request,
            HttpServletResponse response) {
        authService.completeSignup(
            request.signupToken(), request.nickname(),
            request.birth(), request.gender(), response);
        return ResponseEntity.ok(Map.of("message", "signup.complete"));
    }

    // 닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam String nickname) {
        boolean available = authService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 토큰 갱신
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletResponse response) {
        authService.refreshTokens(response);
        return ResponseEntity.ok().build();
    }
}
