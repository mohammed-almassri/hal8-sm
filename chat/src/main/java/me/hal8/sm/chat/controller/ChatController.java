package me.hal8.sm.chat.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.hal8.sm.chat.dto.generic.ResponseDTO;
import me.hal8.sm.chat.dto.request.ChatRequest;
import me.hal8.sm.chat.dto.response.ChatResponse;
import me.hal8.sm.chat.entity.CustomUserDetails;
import me.hal8.sm.chat.service.IChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/chat", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class ChatController {

    IChatService chatService;

   @PostMapping
    public ResponseEntity<ResponseDTO<ChatResponse>> create(@Valid @RequestBody ChatRequest chatDTO){
       var details = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       var chat = chatService.createChat(chatDTO,details.getId());
       return ResponseEntity.ok(new ResponseDTO<>("chat created successfully",chat));
   }

   @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ChatResponse>> find(@PathVariable String id){
       var chat = chatService.findChatById(UUID.fromString(id));
       var auth = SecurityContextHolder.getContext().getAuthentication();
       var details = (CustomUserDetails)auth.getPrincipal();
       var participant = chat.getParticipants().stream().anyMatch(p->p.getParticipantId().equals(details.getId()));
       if(!participant){
           throw new RuntimeException("no");
       }
       return ResponseEntity.ok(new ResponseDTO<>("success",chat));
   }

}
