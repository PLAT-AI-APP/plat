package com.plat.platdata.entity.character.hashtag;

import com.plat.platdata.entity.character.CharacterLanguagePack;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_lang_pack_id")
    private CharacterLanguagePack languagePack;

    @Builder
    public Tag(Hashtag hashtag, CharacterLanguagePack languagePack) {
        this.hashtag = hashtag;
        this.languagePack = languagePack;
    }
}
