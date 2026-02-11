package com.plat.platdata.auth.jwt.annotation;

import com.plat.platdata.auth.jwt.CustomJwtToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUserContextHolder {

    public static CustomJwtToken getJwtToken() {
        return (CustomJwtToken) SecurityContextHolder.getContext().getAuthentication();
    }
}
