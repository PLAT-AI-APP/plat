package com.plat.platdata.auth.dto;

import com.plat.platdata.domain.user.enums.Role;

import java.util.List;

public record UserAuthToken(Long userId,
                            String provider,
                            String providerId,
                            List<Role> roles
) { }
