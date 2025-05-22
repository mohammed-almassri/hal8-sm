package me.hal8.sm.posts.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.repository.PostRepository;
import me.hal8.sm.posts.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;


    public List<Post> getAllPosts() {
        return postRepository.findAllNotDeleted();
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findByIdNotDeleted(id);
    }

    public List<Post> getPostsByUserId(UUID userId) {
        return postRepository.findByUserId(userId);
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Transactional
    public Optional<Post> updatePost(String id, Post postDetails) {
        return postRepository.findByIdNotDeleted(id)
                .map(existingPost -> {
                    existingPost.setContent(postDetails.getContent());
                    return postRepository.save(existingPost);
                });
    }

    @Transactional
    public boolean deletePost(String id) {
        return postRepository.findByIdNotDeleted(id)
                .map(post -> {
                    post.softDelete();
                    postRepository.save(post);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<Post> likePost(String postId, String userId) {
        return postRepository.findByIdNotDeleted(postId)
                .map(post -> {
                    List<String> likedBy = post.getLikedBy();
                    if(likedBy==null){
                        likedBy = new ArrayList<>();
                    }
                    if (!likedBy.contains(userId)) {
                        likedBy.add(userId);
                        post.setLikes(post.getLikes() + 1);
                        return postRepository.save(post);
                    }
                    return post;
                });
    }

    @Transactional
    public Optional<Post> unlikePost(String postId, String userId) {
        return postRepository.findByIdNotDeleted(postId)
                .map(post -> {
                    List<String> likedBy = post.getLikedBy();
//                    if(likedBy==null){
//                        likedBy = new ArrayList<>();
//                    }

//                    if (likedBy.remove(userId)) {
                        post.setLikes(post.getLikes() - 1);
                        return postRepository.save(post);
//                    }
//                    return post;
                });
    }

    @Transactional
    public Optional<Post> sharePost(String postId, String userId) {
        return postRepository.findByIdNotDeleted(postId)
                .map(post -> {
                    List<String> sharedBy = post.getSharedBy();
                    if (!sharedBy.contains(userId)) {
                        sharedBy.add(userId);
                        post.setShares(post.getShares() + 1);
                        return postRepository.save(post);
                    }
                    return post;
                });
    }
}
