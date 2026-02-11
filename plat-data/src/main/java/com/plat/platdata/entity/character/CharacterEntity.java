package com.plat.platdata.entity.character;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.enums.*;
import com.plat.platdata.entity.character.lorebook.Lorebook;
import com.plat.platdata.entity.character.scenario.Scenario;
import com.plat.platdata.domain.user.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`character`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Creator creator;

    @Column(name = "cover_image")
    private String coverImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "height", columnDefinition = "TEXT")
    private String height;

    @Column(name = "weight", columnDefinition = "TEXT")
    private String weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "tendency", nullable = false)
    private Tendency tendency = Tendency.ALL;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "adult_only", nullable = false)
    private AdultOnly adultOnly = AdultOnly.ALL;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility = Visibility.PRIVATE;

    @Enumerated(EnumType.STRING)
    @Column(name = "review_status", nullable = false)
    private ReviewStatus reviewStatus = ReviewStatus.PENDING;

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String reviewComment;

    @Enumerated(EnumType.STRING)
    @Column(name = "original_language")
    private Language originalLanguage = Language.KO;

    @Column(name = "total_chat_count", nullable = false)
    private Integer totalChatCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    // 언어 팩들과의 관계
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CharacterLanguagePack> languagePacks = new ArrayList<>();

    // 시나리오 (Character 삭제 시 cascade)
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scenario> scenarios = new ArrayList<>();

    // 로어북 (Character 삭제 시 cascade)
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lorebook> lorebooks = new ArrayList<>();

    @Builder
    public CharacterEntity(Creator creator, String coverImage, Gender gender, String height,
                           String weight, Tendency tendency, Category category, AdultOnly adultOnly,
                           Visibility visibility, ReviewStatus reviewStatus, String reviewComment,
                           Language originalLanguage) {
        this.creator = creator;
        this.coverImage = coverImage;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.tendency = tendency != null ? tendency : Tendency.ALL;
        this.category = category;
        this.adultOnly = adultOnly != null ? adultOnly : AdultOnly.ALL;
        this.visibility = visibility != null ? visibility : Visibility.PRIVATE;
        this.reviewStatus = reviewStatus != null ? reviewStatus : ReviewStatus.PENDING;
        this.reviewComment = reviewComment;
        this.originalLanguage = originalLanguage != null ? originalLanguage : Language.KO;
        this.totalChatCount = 0;
        this.likeCount = 0;
    }

}
