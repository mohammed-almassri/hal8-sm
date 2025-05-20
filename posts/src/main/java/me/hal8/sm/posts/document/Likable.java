package me.hal8.sm.posts.document;

import lombok.Data;

import java.util.List;

@Data
public abstract class Likable extends BaseEntity{
    private List<String> likedBy;
    private List<String> sharedBy;
    private int likes = 0;
    private int shares = 0;
}
