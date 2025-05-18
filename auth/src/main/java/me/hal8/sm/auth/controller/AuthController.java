package me.hal8.sm.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.hal8.sm.auth.dto.LoginRequestDTO;
import me.hal8.sm.auth.dto.RegisterRequestDTO;
import me.hal8.sm.auth.dto.UserResponseDTO;
import me.hal8.sm.auth.service.IUserService;
import me.hal8.sm.auth.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final IUserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO userRequest) {
        var user = userService.createUser(userRequest);
        String jwt = jwtUtil.generateToken(user.getEmail(),user.getId(),"");
        user.setToken(jwt);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid LoginRequestDTO userRequest) {
        var user = userService.loginUser(userRequest);
        String jwt = jwtUtil.generateToken(user.getEmail(),user.getId(),"");
        user.setToken(jwt);
        return ResponseEntity.ok(user);
    }

}
