package me.hal8.sm.posts.service;

import me.hal8.sm.posts.document.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> getCommentsByPostId(String postId);
    Optional<Comment> getCommentById(String id);
    Optional<Comment> createComment(String postId, Comment comment);
    Optional<Comment> updateComment(String id, Comment commentDetails);
    boolean deleteComment(String id);
    Optional<Comment> addReply(String commentId, Comment reply);
    Optional<Comment> likeComment(String commentId, String userId);
    Optional<Comment> unlikeComment(String commentId, String userId);
}
