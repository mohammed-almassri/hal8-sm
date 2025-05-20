package com.microtwitter.followservice.dto;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDTO {

    private UUID id;

    private String name;

    private String email;

    private String token;
}
