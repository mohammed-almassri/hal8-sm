package me.hal8.sm.auth.mapper;

import me.hal8.sm.auth.dto.UserResponseDTO;
import me.hal8.sm.auth.entity.User;

public class UserMapper {
    public static UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
//        dto.setRoles(user.getRoles());
        return dto;
    }
}
