package me.hal8.sm.posts.repository;

import me.hal8.sm.posts.document.UserFeed;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserFeedRepository extends MongoRepository<UserFeed, UUID> {
}