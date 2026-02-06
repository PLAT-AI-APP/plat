package com.plat.platai.entity;

import com.plat.platai.entity.enums.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Persona {

    private Long id;

    // ── 프로필 ──
    private Language language;
    private String title;
    private String name;
    private String introduction;
    private String characterInfo;
    private Gender gender;
    private Integer height;
    private Integer weight;
    private List<String> jobs = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<String> likes = new ArrayList<>();
    private List<String> dislikes = new ArrayList<>();
    private String detailedSetting;

    // ── 캐릭터 에셋 (1~10) ──
    private List<PersonaAsset> assets = new ArrayList<>();
    // ── 로어북 (최대 5) ──
    private List<PersonaLorebook> lorebooks = new ArrayList<>();
    // ── 상황설정 (1~5) ──
    private List<PersonaScenario> scenarios = new ArrayList<>();
    // ── 상세정보 (검색용) ──
    private Orientation orientation;
    private AgeVerification ageVerification;
    private List<Category> categories = new ArrayList<>();
    private List<String> hashtags = new ArrayList<>();
    private Visibility visibility;
}