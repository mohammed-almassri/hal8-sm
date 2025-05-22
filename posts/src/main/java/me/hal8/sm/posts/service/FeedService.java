package me.hal8.sm.posts.service;

import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.dto.event.FollowCreatedEvent;

import java.util.List;
import java.util.UUID;

public interface FeedService {
    public void handleFollow(FollowCreatedEvent event);
    public List<Post> getFeed(UUID userId);
}
