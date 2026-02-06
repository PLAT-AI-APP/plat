package com.plat.platai.entity;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonaAsset {

    private Long id;
    private Persona persona;
    private String imageUrl;
    private String fileType;
    private Long fileSize;
    private Integer sortOrder;
}