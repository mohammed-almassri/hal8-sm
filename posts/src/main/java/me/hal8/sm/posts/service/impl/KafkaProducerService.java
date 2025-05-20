package me.hal8.sm.posts.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.config.KafkaConfig;
import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.dto.event.PostCreatedEvent;
import me.hal8.sm.posts.dto.event.PostLikedEvent;
import me.hal8.sm.posts.service.EventProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class KafkaProducerService implements EventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPostCreatedEvent(Post post) {
        PostCreatedEvent event = PostCreatedEvent.builder()
                .postId(post.getId())
                .userId(post.getUserId())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
            .build();

        kafkaTemplate.send(KafkaConfig.TOPIC_POST_CREATED, post.getId(), event);
    }

    public void sendPostLikedEvent(Post post, UUID userId) {
        PostLikedEvent event = PostLikedEvent.builder()
                .postId(post.getId())
                .userId(userId)
                .totalLikes(post.getLikes())
                .likedAt(Instant.now())
                .build();

        kafkaTemplate.send(KafkaConfig.TOPIC_POST_LIKED, post.getId(), event);
    }
}
