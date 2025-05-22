package com.microtwitter.followservice.service;

import com.microtwitter.followservice.config.KafkaConfig;
import com.microtwitter.followservice.dto.FollowCreatedEvent;
import com.microtwitter.followservice.model.Follow;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendFollowCreatedEvent(Follow post) {
        var event = FollowCreatedEvent.builder()
                .followedId(post.getFollowingId())
                .followerId(post.getFollowerId())
            .build();

        kafkaTemplate.send(KafkaConfig.TOPIC_FOLLOW_CREATED, post.getId().toString(), event);
    }

}
