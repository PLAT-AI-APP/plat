package com.plat.platdata.entity.character.lorebook;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.CharacterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lorebook")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lorebook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lorebook_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private CharacterEntity character;

    @OneToMany(mappedBy = "lorebook", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<LorebookTranslation> lorebookTranslations = new ArrayList<>();

}
