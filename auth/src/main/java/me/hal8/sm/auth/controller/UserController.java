package me.hal8.sm.auth.controller;

import lombok.AllArgsConstructor;
import me.hal8.sm.auth.dto.UserResponseDTO;
import me.hal8.sm.auth.entity.CustomUserDetails;
import me.hal8.sm.auth.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    IUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getUsers(@RequestParam int page, @RequestParam int size){
        var users = userService.findUsers( PageRequest.of(page,size));
        return ResponseEntity.ok(users);
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(){
        var id = (CustomUserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        var user = userService.findUserByEmail(id.getUsername());
        return ResponseEntity.ok(user);
    }
}
