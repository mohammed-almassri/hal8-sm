package me.hal8.sm.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class ChatParticipantResponse {
    private UUID participantId;
    private boolean isAdmin;
    private Instant joinedAt;
}
