package me.hal8.sm.posts.service.impl;

import me.hal8.sm.posts.config.KafkaConfig;
import me.hal8.sm.posts.dto.event.RecGeneratedEvent;
import me.hal8.sm.posts.service.RecListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaRecListener implements RecListener {

    @KafkaListener(topics = KafkaConfig.TOPIC_REC_GENERATED, groupId = "social-media-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleRecommendation(RecGeneratedEvent event) {
        System.out.println("Received recommendation for post: " + event.getPostId());
    }
}
