package com.plat.platboot.auth.dto;

public record SignupTokenResponse(
    String signupToken,
    boolean newUser
) {}
