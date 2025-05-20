package me.hal8.sm.posts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private String id;
    private String content;
    private UUID userId;
    private int likes;
    private int shares;
    private List<CommentResponse> comments;
    private Instant createdAt;
    private Instant updatedAt;
}
