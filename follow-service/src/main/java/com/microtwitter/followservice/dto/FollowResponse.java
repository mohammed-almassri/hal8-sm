package com.microtwitter.followservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FollowResponse {
    private Long id;
    private Long followerId;
    private Long followingId;
    private LocalDateTime createdAt;
}
