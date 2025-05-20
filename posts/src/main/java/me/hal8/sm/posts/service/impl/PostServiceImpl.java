package me.hal8.sm.posts.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.posts.repository.PostReposirtory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostServiceImpl {
    private final PostReposirtory postReposirtory;
}
