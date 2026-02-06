package com.plat.platdata.entity.character;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.chat.enums.CreatorGrade;
import com.plat.platdata.entity.chat.enums.CreatorStatus;
import com.plat.platdata.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "creator")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Creator extends BaseEntity {

    @Id
    @Column(name = "creator_id")
    private Long id;

    @MapsId // User의 PK를 Creator의 PK로 사용
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "activity_name", nullable = false, length = 50)
    private String activityName;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade", nullable = false)
    private CreatorGrade grade = CreatorGrade.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CreatorStatus status = CreatorStatus.APPROVED;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Builder
    public Creator(User user, String activityName, String introduction,
                   CreatorGrade grade, CreatorStatus status) {
        this.user = user;
        this.activityName = activityName;
        this.introduction = introduction;
        this.grade = grade != null ? grade : CreatorGrade.NEW;
        this.status = status != null ? status : CreatorStatus.APPROVED;

        if (this.status == CreatorStatus.APPROVED) {
            this.approvedAt = LocalDateTime.now();
        }
    }


}
