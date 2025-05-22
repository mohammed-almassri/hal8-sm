package me.hal8.sm.posts.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowCreatedEvent {
    String  followerId;
    String followedId;
}
