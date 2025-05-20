package me.hal8.sm.posts.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collation = "comments")
@Data
public class Comment extends Likable{
    private String postId;
    private String content;
    private List<Comment> replies;
}
