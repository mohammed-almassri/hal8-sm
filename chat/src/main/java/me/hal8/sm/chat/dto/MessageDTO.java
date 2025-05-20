package me.hal8.sm.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Message")
public class MessageDTO {

    private UUID id;

    @Schema(required = true)
    @NotNull(message = "chatId is required")
    private UUID chatId;

    @Schema(required = true)
    @NotNull(message = "senderId is required")
    private UUID senderId;

    @Schema( required = true)
    @NotEmpty(message = "content is required")
    private String content;
}
