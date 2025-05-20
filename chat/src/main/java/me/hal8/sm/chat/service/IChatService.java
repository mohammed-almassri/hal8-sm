package me.hal8.sm.chat.service;

import me.hal8.sm.chat.dto.request.ChatParticipantRequest;
import me.hal8.sm.chat.dto.request.ChatRequest;
import me.hal8.sm.chat.dto.response.ChatResponse;

import java.util.List;
import java.util.UUID;

public interface IChatService {
    ChatResponse createChat(ChatRequest chatDTO, UUID creatorId);
    ChatResponse findChatById(UUID id);
    ChatResponse updateParticipants(ChatRequest chatDTO, List<ChatParticipantRequest> chatParticipantDTOS);
}
