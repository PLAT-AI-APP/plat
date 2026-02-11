package com.plat.platdata.domain.user;

import com.plat.platdata.domain.user.enums.Gender;
import com.plat.platdata.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private final Long id;
    private final String nickname;
    private final LocalDateTime birth;
    private final Gender gender;
    private final Role role;
    private final String profileImage;
    private final String phone;
    private final Boolean adultVerified;
}
