package me.hal8.sm.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class MessageResponse {
    private UUID id;
    private UUID chatId;
    private String content;
    private ChatParticipantResponse sender;
}
