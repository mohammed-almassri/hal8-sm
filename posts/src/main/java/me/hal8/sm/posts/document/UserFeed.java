package me.hal8.sm.posts.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "feeds")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeed {
    @Id
    private UUID userId;

    private List<Post> posts;
}