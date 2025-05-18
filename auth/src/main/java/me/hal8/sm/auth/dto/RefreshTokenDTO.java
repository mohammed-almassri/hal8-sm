package me.hal8.sm.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "RefreshTokenRequest")
public class RefreshTokenDTO {

    @Schema(description = "Refresh token string")
    private String refreshToken;
}
