package com.plat.platdata.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    USER("ROLE_USER"),
    CREATOR("ROLE_CREATOR"),
    ADMIN("ROLE_ADMIN");

    private final String role;
}
