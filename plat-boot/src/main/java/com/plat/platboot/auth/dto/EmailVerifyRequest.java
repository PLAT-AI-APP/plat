package com.plat.platboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailVerifyRequest(
    @NotBlank(message = "{email.notblank}")
    @Email(message = "{email.invalid}")
    String email
) {}
