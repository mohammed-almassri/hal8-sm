package me.hal8.sm.posts.service.impl;

import lombok.RequiredArgsConstructor;
import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.document.UserFeed;
import me.hal8.sm.posts.dto.event.FollowCreatedEvent;
import me.hal8.sm.posts.repository.PostRepository;
import me.hal8.sm.posts.repository.UserFeedRepository;
import me.hal8.sm.posts.service.FeedService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final PostRepository postRepository;
    private final UserFeedRepository userFeedRepository;

    // When a follow event occurs
    public void handleFollow(FollowCreatedEvent event) {
        UUID followedId = UUID.fromString(event.getFollowedId());
        String followerId = event.getFollowerId();

        List<Post> followedPosts = postRepository.findByUserId(followedId);

        UserFeed feed = userFeedRepository.findById(UUID.fromString(followerId))
                .orElse(new UserFeed(UUID.fromString(followerId), followedPosts));

        feed.getPosts().addAll(followedPosts);
        userFeedRepository.save(feed);
    }

    // Fetch a user's feed
    public List<Post> getFeed(UUID userId) {
        return userFeedRepository.findById(userId)
                .map(UserFeed::getPosts)
                .orElse(List.of());
    }
}

