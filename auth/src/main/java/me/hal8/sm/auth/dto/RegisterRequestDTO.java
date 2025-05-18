package me.hal8.sm.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "RegisterRequest")
public class RegisterRequestDTO {

    @Schema(description = "User's full name", example = "Jane Doe")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Email address", example = "jane@example.com")
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Password (min 8 characters)", example = "strongPass123")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
