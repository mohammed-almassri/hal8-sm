package me.hal8.sm.auth.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(name = "UserResponse")
public class UserResponseDTO {

    @Schema(description = "User ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "User full name", example = "Jane Doe")
    private String name;

    @Schema(description = "Email address", example = "jane@example.com")
    private String email;

    private String token;
}
