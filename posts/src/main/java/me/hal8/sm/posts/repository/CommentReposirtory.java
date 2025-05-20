package me.hal8.sm.posts.repository;

import me.hal8.sm.posts.document.Comment;
import me.hal8.sm.posts.document.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReposirtory extends MongoRepository<Comment,String> {
}
