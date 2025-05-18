package me.hal8.sm.auth.service.impl;

import lombok.AllArgsConstructor;
import me.hal8.sm.auth.dto.LoginRequestDTO;
import me.hal8.sm.auth.dto.RegisterRequestDTO;
import me.hal8.sm.auth.dto.UserResponseDTO;
import me.hal8.sm.auth.entity.User;
import me.hal8.sm.auth.mapper.UserMapper;
import me.hal8.sm.auth.repository.UserRepository;
import me.hal8.sm.auth.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO findUserById(UUID id) {
        var user = userRepository.findById(id).orElseThrow(()->{throw new RuntimeException("no user with id: "+id);});
        return UserMapper.mapToDTO(user);
    }

    @Override
    public UserResponseDTO findUserByEmail(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(()->{throw new RuntimeException("no user with email: "+email);});
        return UserMapper.mapToDTO(user);
    }

    @Override
    public UserResponseDTO createUser(RegisterRequestDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())){
            throw new RuntimeException("user with email "+registerDTO.getEmail()+"already exists");
        }

        User user = new User();
        user.setName(registerDTO.getName().split(" ")[0]);
        user.setEmail(registerDTO.getEmail());
        var hashed = passwordEncoder.encode(registerDTO.getPassword());
        user.setPassword(hashed);
        user.setRoles("STUDENT");
        return UserMapper.mapToDTO(userRepository.save(user));

    }

    @Override
    public UserResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        var user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()->{
            throw new RuntimeException("no user with email: "+loginRequestDTO.getEmail());
        });
        var hashed = passwordEncoder.encode(loginRequestDTO.getPassword());
        if(passwordEncoder.matches(user.getPassword(),hashed)){
            throw new RuntimeException("incorrect password");
        }
        return UserMapper.mapToDTO(user);
    }

    @Override
    public Page<UserResponseDTO> findUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).map(UserMapper::mapToDTO);
    }

}
