package me.hal8.sm.posts.service;

import me.hal8.sm.posts.dto.event.FollowCreatedEvent;
import me.hal8.sm.posts.dto.event.RecGeneratedEvent;

public interface RecListener {
//    public void handleRecommendation(RecGeneratedEvent event);
    public void handleFollow(FollowCreatedEvent event);
}
