package me.hal8.sm.chat.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.hal8.sm.chat.dto.generic.ResponseDTO;
import me.hal8.sm.chat.dto.request.MessageRequest;
import me.hal8.sm.chat.dto.response.MessageResponse;
import me.hal8.sm.chat.service.IMessageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/message", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class MessageController {

    IMessageService messageService;

   @PostMapping
    public ResponseEntity<ResponseDTO<MessageResponse>> create(@Valid @RequestBody MessageRequest messageDTO){
       var message = messageService.createMessage(messageDTO);
       return ResponseEntity.ok(new ResponseDTO<>("message created successfully",message));
   }

   @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<MessageResponse>> find(@PathVariable String id){
       var message = messageService.findMessageById(UUID.fromString(id));
       return ResponseEntity.ok(new ResponseDTO<>("success",message));
   }
}
