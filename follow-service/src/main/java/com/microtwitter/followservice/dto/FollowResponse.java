package com.microtwitter.followservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FollowResponse {
    private Long id;
    private UUID followerId;
    private UUID followingId;
    private LocalDateTime createdAt;
}
