package com.plat.platai.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonaLorebook {

    private Long id;
    private Persona persona;
    private String content;
    private List<String> keywords = new ArrayList<>();
}