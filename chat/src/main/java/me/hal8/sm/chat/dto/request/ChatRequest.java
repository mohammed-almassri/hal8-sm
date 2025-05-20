package me.hal8.sm.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ChatRequest {
    @Schema(
            description = "the type of the chat, must be either PRIVATE or GROUP"
    )
    @Pattern(regexp = "PRIVATE|GROUP", message = "Chat type must be either PRIVATE or GROUP")
    private String type;

    @NotEmpty(message = "name is required")
    private String name;

    @NotNull(message = "participant list is required")
    @Size(min = 1,max = 256, message = "Chat must have at least 1 participants")
    @Valid
    private List<ChatParticipantRequest> participants;
}
