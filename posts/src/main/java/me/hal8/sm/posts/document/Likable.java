package me.hal8.sm.posts.document;

import java.util.List;

public abstract class Likable extends BaseEntity{
    private List<String> likedBy;
    private List<String> sharedBy;
    private int likes = 0;
    private int shares = 0;
}
