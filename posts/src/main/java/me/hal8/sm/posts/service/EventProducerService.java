package me.hal8.sm.posts.service;

import me.hal8.sm.posts.document.Post;

import java.util.UUID;

public interface EventProducerService {
    void sendPostCreatedEvent(Post post);
    void sendPostLikedEvent(Post post, UUID userId);
}
