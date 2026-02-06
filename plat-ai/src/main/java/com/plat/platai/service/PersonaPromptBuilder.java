package com.plat.platai.service;

import com.plat.platai.dto.LongTermMemory;
import com.plat.platai.entity.Persona;
import com.plat.platai.entity.PersonaLorebook;
import com.plat.platai.entity.PersonaScenario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PersonaPromptBuilder {

    @Qualifier("platformRules")
    private final String platformRules;

    /**
     * 4계층 시스템 프롬프트 빌드
     * Layer 1: 플랫폼 규칙 (platformRules)
     * Layer 2: 캐릭터 설정 (persona + scenario + lorebook)
     * Layer 3: 장기기억 (longTermMemory)
     * Layer 4: 대화 히스토리 → Message 객체로 분리 (여기에 포함하지 않음)
     */
    public String buildSystemPrompt(Persona persona, PersonaScenario scenario,
                                    LongTermMemory longTermMemory, List<PersonaLorebook> activeLorebook) {
        StringBuilder prompt = new StringBuilder();

        // === Layer 1: 플랫폼 규칙 ===
        prompt.append(platformRules).append("\n\n");

        prompt.append("<Character_Setting>\n");
        // === Layer 2: 캐릭터 설정 ===
        prompt.append("당신은 '").append(persona.getName()).append("' 캐릭터입니다.\n\n");

        prompt.append("# 기본 정보\n");
        prompt.append("- 제목: ").append(persona.getTitle()).append("\n");
        prompt.append("- 소개: ").append(persona.getIntroduction()).append("\n");
        if (persona.getHashtags() != null && !persona.getHashtags().isEmpty()) {
            prompt.append("- 태그: ").append(String.join(", ", persona.getHashtags())).append("\n");
        }
        prompt.append("\n");

        prompt.append("# 신체 정보\n");
        prompt.append("- 성별: ").append(persona.getGender()).append("\n");
        if (persona.getHeight() != null) {
            prompt.append("- 키: ").append(persona.getHeight()).append("cm\n");
        }
        if (persona.getWeight() != null) {
            prompt.append("- 몸무게: ").append(persona.getWeight()).append("kg\n");
        }
        prompt.append("\n");

        prompt.append("# 직업 및 배경\n");
        if (persona.getJobs() != null && !persona.getJobs().isEmpty()) {
            prompt.append("- 직업: ").append(String.join(", ", persona.getJobs())).append("\n");
        }
        prompt.append("\n");

        prompt.append("# 성격 및 특성\n");
        if (persona.getCharacterInfo() != null) {
            prompt.append("- 캐릭터 정보: ").append(persona.getCharacterInfo()).append("\n");
        }
        if (persona.getInterests() != null && !persona.getInterests().isEmpty()) {
            prompt.append("- 관심사: ").append(String.join(", ", persona.getInterests())).append("\n");
        }
        if (persona.getLikes() != null && !persona.getLikes().isEmpty()) {
            prompt.append("- 좋아하는 것: ").append(String.join(", ", persona.getLikes())).append("\n");
        }
        if (persona.getDislikes() != null && !persona.getDislikes().isEmpty()) {
            prompt.append("- 싫어하는 것: ").append(String.join(", ", persona.getDislikes())).append("\n");
        }
        prompt.append("\n");

        prompt.append("# 상세설정\n");
        prompt.append(persona.getDetailedSetting()).append("\n\n");

        // 로어북 (키워드 필터링된 활성 로어북만)
        if (activeLorebook != null && !activeLorebook.isEmpty()) {
            prompt.append("# 로어북\n");
            for (PersonaLorebook lorebook : activeLorebook) {
                prompt.append("- [").append(String.join(", ", lorebook.getKeywords())).append("] ");
                prompt.append(lorebook.getContent()).append("\n");
            }
            prompt.append("\n");
        }

        prompt.append("\n</Character_Setting>\n\n");


        // 상황설정
        if (scenario != null) {
            prompt.append("# 현재 상황\n");
            prompt.append("- 장소: ").append(scenario.getLocation()).append("\n");
            prompt.append("- 상황: ").append(scenario.getInitialSituation()).append("\n");
            prompt.append("- 첫 대사: ").append(scenario.getInitialDialogue()).append("\n\n");
        }

        // === Layer 3: 장기기억 ===
        if (longTermMemory != null && longTermMemory.getSummary() != null) {
            prompt.append("<LongTermMemory>\n>");
            prompt.append(longTermMemory.getSummary());
            prompt.append("\n</LongTermMemory>\n\n>");
        }

        return prompt.toString();
    }

    /**
     * 최근 대화 컨텍스트에서 키워드 매칭되는 로어북만 필터링
     */
    public List<PersonaLorebook> filterActiveLorebook(Persona persona, String recentContext) {
        if (persona.getLorebooks() == null || persona.getLorebooks().isEmpty()) {
            return new ArrayList<>();
        }
        if (recentContext == null || recentContext.isEmpty()) {
            return new ArrayList<>(persona.getLorebooks());
        }

        String lowerContext = recentContext.toLowerCase();
        List<PersonaLorebook> active = new ArrayList<>();

        for (PersonaLorebook lorebook : persona.getLorebooks()) {
            boolean matched = lorebook.getKeywords().stream()
                .anyMatch(keyword -> lowerContext.contains(keyword.toLowerCase()));
            if (matched) {
                active.add(lorebook);
            }
        }

        return active;
    }

    /**
     * 하위 호환용 간단 프롬프트
     */
    public String buildSimplePrompt(Persona context) {
        return String.format(
            "당신은 '%s' 캐릭터입니다. %s\n" +
                "상세설정: %s\n\n" +
                "응답 형식: 행위나 묘사는 **{텍스트}** 형식으로, 대화는 일반 텍스트로 작성하세요.",
            context.getName(),
            context.getIntroduction(),
            context.getDetailedSetting()
        );
    }
}
