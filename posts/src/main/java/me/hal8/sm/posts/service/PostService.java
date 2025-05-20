package me.hal8.sm.posts.service;

import me.hal8.sm.posts.document.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts();
    Optional<Post> getPostById(String id);
    List<Post> getPostsByUserId(UUID userId);
    Post createPost(Post post);
    Optional<Post> updatePost(String id, Post postDetails);
    boolean deletePost(String id);
    Optional<Post> likePost(String postId, String userId);
    Optional<Post> unlikePost(String postId, String userId);
    Optional<Post> sharePost(String postId, String userId);
}
