package me.hal8.sm.posts.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecGeneratedEvent {
    private String postId;
    private List<String> users;
}
