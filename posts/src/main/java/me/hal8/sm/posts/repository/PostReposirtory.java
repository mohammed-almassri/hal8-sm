package me.hal8.sm.posts.repository;

import me.hal8.sm.posts.document.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReposirtory extends MongoRepository<Post,String> {
}
