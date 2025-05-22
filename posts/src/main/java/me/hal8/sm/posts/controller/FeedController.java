package me.hal8.sm.posts.controller;

import lombok.RequiredArgsConstructor;
import me.hal8.sm.posts.document.CustomUserDetails;
import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.service.FeedService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public List<Post> getUserFeed() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = ((CustomUserDetails)auth.getPrincipal()).getId();
        return feedService.getFeed(userId);
    }
}

