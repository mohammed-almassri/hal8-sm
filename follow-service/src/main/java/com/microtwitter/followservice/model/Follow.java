package com.microtwitter.followservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "follows")
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID followerId;

    @Column(nullable = false)
    private UUID followingId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Add unique constraint to prevent duplicate follows
    @Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"followerId", "followingId"})
    })
    private static class UniqueFollowConstraint {}
}
