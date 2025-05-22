package me.hal8.sm.posts.controller;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.document.CustomUserDetails;
import me.hal8.sm.posts.document.Post;
import me.hal8.sm.posts.dto.request.PostRequest;
import me.hal8.sm.posts.service.EventProducerService;
import me.hal8.sm.posts.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final EventProducerService eventProducerService;

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        var posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        return postService.getPostById(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUserId(@PathVariable UUID userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequestDto) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = ((CustomUserDetails)auth.getPrincipal()).getId();
        var post = new Post();
        post.setContent(postRequestDto.getContent());
        post.setUserId(userId);
        Post newPost = postService.createPost(post);
        eventProducerService.sendPostCreatedEvent(newPost);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<PostResponseDto> updatePost(@PathVariable String id, @RequestBody PostRequestDto postRequestDto) {
//        return postService.getPostById(id)
//                .map(post -> {
//                    postMapper.updateEntityFromDto(postRequestDto, post);
//                    Post updatedPost = postService.createPost(post);
//                    return new ResponseEntity<>(postMapper.toDto(updatedPost), HttpStatus.OK);
//                })
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        boolean deleted = postService.deletePost(id);
        return deleted ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Post> likePost(
            @PathVariable String id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = ((CustomUserDetails)auth.getPrincipal()).getId();
        var p =  postService.likePost(id, userId.toString()).get();
        eventProducerService.sendPostLikedEvent(p,userId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/{id}/unlike")
    public ResponseEntity<Post> unlikePost(
            @PathVariable String id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = ((CustomUserDetails)auth.getPrincipal()).getId();
        var p =  postService.unlikePost(id, userId.toString()).get();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
//
//    @PostMapping("/{id}/share")
//    public ResponseEntity<PostResponseDto> sharePost(
//            @PathVariable String id,
//            @RequestBody ShareRequestDto shareRequestDto) {
//        return postService.sharePost(id, shareRequestDto.getUserId())
//                .map(post -> new ResponseEntity<>(postMapper.toDto(post), HttpStatus.OK))
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
}
