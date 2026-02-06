package com.plat.platdata.entity.character.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    SIMULATION("시뮬레이션"),
    ROMANCE("로맨스"),
    FANTASY_SF("판타지/SF"),
    DRAMA("드라마"),
    MARTIAL_ARTS("무협/사극"),
    GL("GL"),
    BL("BL"),
    HORROR_MYSTERY("공포/추리");

    private final String description;
}
