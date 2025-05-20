package me.hal8.sm.posts.service;

import me.hal8.sm.posts.document.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Page<Post> findAll(Pageable pageable);
    Optional<Post> findById(String id);
}
