package me.hal8.sm.posts.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collation = "posts")
@Data
public class Post extends Likable{
    private String content;
    private UUID userId;
    private List<Comment> comments;
}
