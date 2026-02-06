package com.plat.platdata.entity.character.hashtag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(name = "hashtag", nullable = false, length = 10)
    private String hashtag;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    private List<HashtagTranslation> translations = new ArrayList<>();

    @Builder
    public Hashtag(String hashtag, List<HashtagTranslation> translations) {
        this.translations = translations;
        this.hashtag = hashtag;
    }
}
