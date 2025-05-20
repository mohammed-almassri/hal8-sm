package com.microtwitter.followservice.repository;

import com.microtwitter.followservice.model.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Page<Follow> findByFollowerId(Long followerId, Pageable pageable);
    Page<Follow> findByFollowingId(Long followingId, Pageable pageable);
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
