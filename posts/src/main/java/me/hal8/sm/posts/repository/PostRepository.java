package me.hal8.sm.posts.repository;

import me.hal8.sm.posts.document.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
    @Query(value="{'userId': ?0, 'deletedAt': null}",collation = "en")
    List<Post> findByUserId(UUID userId);

    @Query(value = "{'id': ?0, 'deletedAt': null}",collation = "en")
    Optional<Post> findByIdNotDeleted(String id);

    @Query(value = "{'deletedAt': null}", collation = "en")
    List<Post> findAllNotDeleted();
}
