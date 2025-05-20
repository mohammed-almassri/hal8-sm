package me.hal8.sm.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id",nullable = false)
    private Chat chat;

    @Column(nullable = false)
    private UUID senderId;

    @Column(nullable = false)
    private String content;
}
