package me.hal8.sm.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class ChatParticipantRequest {
    @NotNull
    private UUID participantId;
}
