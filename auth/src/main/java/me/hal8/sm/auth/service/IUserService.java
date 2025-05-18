package me.hal8.sm.auth.service;

import me.hal8.sm.auth.dto.LoginRequestDTO;
import me.hal8.sm.auth.dto.RegisterRequestDTO;
import me.hal8.sm.auth.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface IUserService {
    UserResponseDTO findUserById(UUID id);
    UserResponseDTO findUserByEmail(String email);
    UserResponseDTO createUser(RegisterRequestDTO registerDTO);
    UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
    Page<UserResponseDTO> findUsers(PageRequest pageRequest);
}
