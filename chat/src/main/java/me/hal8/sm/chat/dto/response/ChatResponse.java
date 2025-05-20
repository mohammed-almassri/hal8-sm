package me.hal8.sm.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
}
