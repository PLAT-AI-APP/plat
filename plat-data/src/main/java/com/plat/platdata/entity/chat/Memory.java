package com.plat.platdata.entity.chat;

import com.plat.platdata.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "memory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memory_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "turn_checkpoint", nullable = false)
    private Integer turnCheckpoint;

    @Builder
    public Memory(ChatRoom chatRoom, String content, Integer turnCheckpoint) {
        this.chatRoom = chatRoom;
        this.content = content;
        this.turnCheckpoint = turnCheckpoint;
    }

}
