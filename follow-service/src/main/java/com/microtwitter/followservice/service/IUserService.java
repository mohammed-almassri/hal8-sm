package com.microtwitter.followservice.service;

import com.microtwitter.followservice.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface IUserService {
    UserResponseDTO findUserByEmail(String email);
}
