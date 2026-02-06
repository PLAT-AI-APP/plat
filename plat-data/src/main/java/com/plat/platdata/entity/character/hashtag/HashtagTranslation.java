package com.plat.platdata.entity.character.hashtag;

import com.plat.platdata.entity.character.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashtagTranslation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_translation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "tag_name")
    private String tagName;

    @Builder
    public HashtagTranslation(Hashtag hashtag, Language language, String tagName) {
        this.hashtag = hashtag;
        this.language = language;
        this.tagName = tagName;
    }
}
