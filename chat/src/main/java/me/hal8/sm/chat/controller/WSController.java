package me.hal8.sm.chat.controller;

import lombok.AllArgsConstructor;
import me.hal8.sm.chat.dto.request.MessageRequest;
import me.hal8.sm.chat.dto.response.MessageResponse;
import me.hal8.sm.chat.service.IMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class WSController {

    private IMessageService messageService;

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public MessageResponse sendMessage(MessageRequest message,
                                       Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("User not authenticated");
        }
        String userId = principal.getName();
        System.out.println("User ID: " + userId);

        var msg = messageService.createMessage(message, UUID.fromString(userId));
        return msg;
    }
}
