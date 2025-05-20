package me.hal8.sm.chat.entity;

import jakarta.persistence.*;

@Entity
public class MessageAttachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Long sizeInBytes;
}
