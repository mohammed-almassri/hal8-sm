package com.microtwitter.followservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FollowRequest {
    @NotNull(message = "User ID to follow is required")
    private UUID followingId;
}
