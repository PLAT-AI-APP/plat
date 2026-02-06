package com.plat.platdata.entity.chat;

import com.plat.platdata.entity.BaseEntity;
import com.plat.platdata.entity.character.enums.Language;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "character_id", nullable = false)
    private Long characterId;

    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    @Column(name = "scenario_id", nullable = false)
    private Long scenarioId;

    @Column(name = "room_uuid", nullable = false, length = 36, unique = true)
    private String roomUuid;

    @Column(name = "room_name", length = 30)
    private String roomName;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language = Language.KO;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @Column(name = "total_turn_count", nullable = false)
    private Integer totalTurnCount = 0;

    // 메시지 (ChatRoom 삭제 시 cascade)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    // 메모리 (ChatRoom 삭제 시 cascade)
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Memory> memories = new ArrayList<>();

    @Builder
    public ChatRoom(Long userId, Long characterId, Long personaId, Long scenarioId,
                    String roomUuid, String roomName, Language language) {
        this.userId = userId;
        this.characterId = characterId;
        this.personaId = personaId;
        this.scenarioId = scenarioId;
        this.roomUuid = roomUuid;
        this.roomName = roomName;
        this.language = language != null ? language : Language.KO;
        this.totalTurnCount = 0;
    }
}
