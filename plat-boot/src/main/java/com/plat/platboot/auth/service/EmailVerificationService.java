package com.plat.platboot.auth.service;

public interface EmailVerificationService {

    void sendVerificationCode(String email);

    boolean verifyCode(String email, String code);
}
