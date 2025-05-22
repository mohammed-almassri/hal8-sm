package me.hal8.sm.chat.service;

import me.hal8.sm.chat.dto.request.MessageRequest;
import me.hal8.sm.chat.dto.response.MessageResponse;

import java.util.UUID;

public interface IMessageService {
    MessageResponse createMessage(MessageRequest messageDTO, UUID userId);
    MessageResponse findMessageById(UUID id);
}
