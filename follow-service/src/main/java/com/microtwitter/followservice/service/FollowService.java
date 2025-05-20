package com.microtwitter.followservice.service;

import com.microtwitter.followservice.dto.FollowListResponse;
import com.microtwitter.followservice.dto.FollowResponse;
import com.microtwitter.followservice.model.Follow;
import com.microtwitter.followservice.repository.FollowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public FollowService(FollowRepository followRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.followRepository = followRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public FollowResponse follow(Long followingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long followerId = getUserIdFromUsername(username); // In real implementation, get from user service or token

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw new IllegalStateException("Already following this user");
        }

        Follow follow = new Follow();
        follow.setFollowerId(followerId);
        follow.setFollowingId(followingId);
        follow.setCreatedAt(LocalDateTime.now());

        Follow savedFollow = followRepository.save(follow);
        
        // Publish follow.created event
        kafkaTemplate.send("follow-events", "follow.created", toFollowResponse(savedFollow));

        return toFollowResponse(savedFollow);
    }

    @Transactional
    public void unfollow(Long followingId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long followerId = getUserIdFromUsername(username); // In real implementation, get from user service or token

        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        
        // Publish follow.deleted event
        kafkaTemplate.send("follow-events", "follow.deleted", 
            Map.of("followerId", followerId, "followingId", followingId));
    }

    public FollowListResponse getFollowers(Long userId, Pageable pageable) {
        Page<Follow> followers = followRepository.findByFollowingId(userId, pageable);
        return toFollowListResponse(followers);
    }

    public FollowListResponse getFollowing(Long userId, Pageable pageable) {
        Page<Follow> following = followRepository.findByFollowerId(userId, pageable);
        return toFollowListResponse(following);
    }

    private FollowListResponse toFollowListResponse(Page<Follow> followPage) {
        FollowListResponse response = new FollowListResponse();
        response.setFollows(followPage.getContent().stream()
            .map(this::toFollowResponse)
            .collect(Collectors.toList()));
        response.setTotalCount(followPage.getTotalElements());
        return response;
    }

    private FollowResponse toFollowResponse(Follow follow) {
        FollowResponse response = new FollowResponse();
        response.setId(follow.getId());
        response.setFollowerId(follow.getFollowerId());
        response.setFollowingId(follow.getFollowingId());
        response.setCreatedAt(follow.getCreatedAt());
        return response;
    }

    // In real implementation, this would either:
    // 1. Extract user ID from the JWT token
    // 2. Call the user service to get the ID
    private Long getUserIdFromUsername(String username) {
        return 1L; // Placeholder implementation
    }
}
