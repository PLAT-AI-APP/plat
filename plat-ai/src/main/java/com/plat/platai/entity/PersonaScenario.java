package com.plat.platai.entity;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonaScenario {

    private Long id;
    private Persona persona;
    private String title;
    private String location;
    private String initialSituation;
    private String initialDialogue;
}