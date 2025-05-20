package me.hal8.sm.posts.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private String id;
    private String postId;
    private String content;
    private int likes;
    private List<CommentResponse> replies;
    private Instant createdAt;
    private Instant updatedAt;
}
