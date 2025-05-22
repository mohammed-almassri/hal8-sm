package com.microtwitter.followservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class FollowCreatedEvent {
    UUID followerId;
    UUID followedId;
}
