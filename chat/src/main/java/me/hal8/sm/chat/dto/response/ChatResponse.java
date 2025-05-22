package me.hal8.sm.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.hal8.sm.chat.entity.Message;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChatResponse {
    private UUID id;
    private String type;
    private String name;
    private List<ChatParticipantResponse> participants;
    private List<Message> messages;
}
