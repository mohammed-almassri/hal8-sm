package me.hal8.sm.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table( indexes = {
        @Index(name = "idx_chat_id", columnList = "chat_id")
})
public class Message extends BaseEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id",nullable = false)
    @JsonBackReference
    private Chat chat;

    @Column(nullable = false)
    private UUID senderId;

    @Column(nullable = false)
    private String content;
}
