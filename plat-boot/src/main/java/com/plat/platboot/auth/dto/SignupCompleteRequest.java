package com.plat.platboot.auth.dto;

import com.plat.platdata.domain.user.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SignupCompleteRequest(
    @NotBlank String signupToken,
    @NotBlank String nickname,
    @NotNull LocalDateTime birth,
    @NotNull Gender gender
) {}
