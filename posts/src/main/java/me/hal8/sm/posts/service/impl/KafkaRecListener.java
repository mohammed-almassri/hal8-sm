package me.hal8.sm.posts.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.config.KafkaConfig;
import me.hal8.sm.posts.dto.event.FollowCreatedEvent;
import me.hal8.sm.posts.dto.event.RecGeneratedEvent;
import me.hal8.sm.posts.service.FeedService;
import me.hal8.sm.posts.service.RecListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class KafkaRecListener implements RecListener {
    private final FeedService feedService;

//    @KafkaListener(topics = KafkaConfig.TOPIC_REC_GENERATED, groupId = "social-media-group", containerFactory = "kafkaListenerContainerFactory")
//    public void handleRecommendation(RecGeneratedEvent event) {
//        System.out.println("Received recommendation for post: " + event.getPostId());
//    }

    @KafkaListener(topics = KafkaConfig.TOPIC_FOLLOW_CREATED, groupId = "social-media-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleFollow(FollowCreatedEvent event) {
        System.out.println("new follow " + event.getFollowedId() + " " + event.getFollowerId());
        feedService.handleFollow(event);
    }
}
