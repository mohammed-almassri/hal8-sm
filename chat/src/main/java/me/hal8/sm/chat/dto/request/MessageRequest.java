package me.hal8.sm.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageRequest {
    @Schema(required = true)
    @NotNull(message = "chatId is required")
    private UUID chatId;

    @Schema( required = true)
    @NotEmpty(message = "content is required")
    private String content;
}
