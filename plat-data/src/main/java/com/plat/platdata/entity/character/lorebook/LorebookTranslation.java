package com.plat.platdata.entity.character.lorebook;

import com.plat.platdata.entity.character.CharacterLanguagePack;
import com.plat.platdata.entity.character.enums.Language;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Entity
@Table(name = "lorebook_translation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LorebookTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lorebook_translation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lorebook_id")
    private Lorebook lorebook;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_lang_pack_id")
    private CharacterLanguagePack languagePack;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ElementCollection
    @CollectionTable(
        name = "lorebook_translation_keyword",
        joinColumns = @JoinColumn(name = "lorebook_translation_id")
    )
    @Column(name = "keyword", nullable = false)
    @Builder.Default
    private List<String> keywords = new ArrayList<>();

}
