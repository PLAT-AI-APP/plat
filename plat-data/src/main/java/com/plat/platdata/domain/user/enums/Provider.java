package com.plat.platdata.domain.user.enums;

public enum Provider {

    KAKAO,
    GOOGLE
    ;

    public static Provider findByProvider(String clientName) {
        return Provider.valueOf(clientName.toUpperCase());
    }

}
