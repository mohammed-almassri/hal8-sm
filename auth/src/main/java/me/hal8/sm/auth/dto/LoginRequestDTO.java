package me.hal8.sm.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest")
public class LoginRequestDTO {
    @Schema(description = "User email", example = "jane@example.com")
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "User password", example = "strongPass123")
    @NotBlank(message = "Password is required")
    private String password;
}
