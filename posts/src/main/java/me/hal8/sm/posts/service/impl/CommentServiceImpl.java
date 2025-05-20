package me.hal8.sm.posts.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.document.Comment;
import me.hal8.sm.posts.repository.CommentRepository;
import me.hal8.sm.posts.repository.PostRepository;
import me.hal8.sm.posts.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }

    public Optional<Comment> getCommentById(String id) {
        return commentRepository.findByIdNotDeleted(id);
    }

    @Transactional
    public Optional<Comment> createComment(String postId, Comment comment) {
        return postRepository.findByIdNotDeleted(postId)
                .map(post -> {
                    comment.setPostId(postId);
                    Comment savedComment = commentRepository.save(comment);
                    List<Comment> comments = post.getComments();
                    comments.add(savedComment);
                    post.setComments(comments);
                    postRepository.save(post);
                    return savedComment;
                });
    }

    @Transactional
    public Optional<Comment> updateComment(String id, Comment commentDetails) {
        return commentRepository.findByIdNotDeleted(id)
                .map(existingComment -> {
                    existingComment.setContent(commentDetails.getContent());
                    return commentRepository.save(existingComment);
                });
    }

    @Transactional
    public boolean deleteComment(String id) {
        return commentRepository.findByIdNotDeleted(id)
                .map(comment -> {
                    comment.softDelete();
                    commentRepository.save(comment);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public Optional<Comment> addReply(String commentId, Comment reply) {
        return commentRepository.findByIdNotDeleted(commentId)
                .map(parentComment -> {
                    reply.setPostId(parentComment.getPostId());
                    Comment savedReply = commentRepository.save(reply);
                    List<Comment> replies = parentComment.getReplies();
                    replies.add(savedReply);
                    parentComment.setReplies(replies);
                    return commentRepository.save(parentComment);
                });
    }

    @Transactional
    public Optional<Comment> likeComment(String commentId, String userId) {
        return commentRepository.findByIdNotDeleted(commentId)
                .map(comment -> {
                    List<String> likedBy = comment.getLikedBy();
                    if (!likedBy.contains(userId)) {
                        likedBy.add(userId);
                        comment.setLikes(comment.getLikes() + 1);
                        return commentRepository.save(comment);
                    }
                    return comment;
                });
    }

    @Transactional
    public Optional<Comment> unlikeComment(String commentId, String userId) {
        return commentRepository.findByIdNotDeleted(commentId)
                .map(comment -> {
                    List<String> likedBy = comment.getLikedBy();
                    if (likedBy.remove(userId)) {
                        comment.setLikes(comment.getLikes() - 1);
                        return commentRepository.save(comment);
                    }
                    return comment;
                });
    }
}
