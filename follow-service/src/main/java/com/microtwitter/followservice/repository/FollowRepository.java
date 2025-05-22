package com.microtwitter.followservice.repository;

import com.microtwitter.followservice.model.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    Page<Follow> findByFollowerId(UUID followerId, Pageable pageable);
    Page<Follow> findByFollowingId(UUID followingId, Pageable pageable);
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
    void deleteByFollowerIdAndFollowingId(UUID followerId, UUID followingId);
}
