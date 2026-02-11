package com.plat.platdata.auth.dto;

import com.plat.platdata.domain.user.enums.AuthType;
import com.plat.platdata.domain.user.enums.Provider;

public record SignupClaims(
    AuthType authType,
    String email,
    String encodedPassword,
    Provider provider,
    String providerId
) {}
