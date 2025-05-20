package me.hal8.sm.posts.repository;

import me.hal8.sm.posts.document.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {
    @Query("{'postId': ?0, 'deletedAt': null}")
    List<Comment> findByPostId(String postId);

    @Query("{'id': ?0, 'deletedAt': null}")
    Optional<Comment> findByIdNotDeleted(String id);
}
