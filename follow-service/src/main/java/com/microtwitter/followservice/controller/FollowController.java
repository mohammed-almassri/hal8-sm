package com.microtwitter.followservice.controller;

import com.microtwitter.followservice.dto.FollowListResponse;
import com.microtwitter.followservice.dto.FollowRequest;
import com.microtwitter.followservice.dto.FollowResponse;
import com.microtwitter.followservice.service.FollowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow")
    public ResponseEntity<FollowResponse> follow(@Valid @RequestBody FollowRequest request) {
        return ResponseEntity.ok(followService.follow(request.getFollowingId()));
    }

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId) {
        followService.unfollow(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<FollowListResponse> getFollowers(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(followService.getFollowers(userId, pageRequest));
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<FollowListResponse> getFollowing(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(followService.getFollowing(userId, pageRequest));
    }
}
