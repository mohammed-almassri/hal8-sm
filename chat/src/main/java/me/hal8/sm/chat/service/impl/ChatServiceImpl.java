package me.hal8.sm.chat.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.hal8.sm.chat.dto.request.ChatParticipantRequest;
import me.hal8.sm.chat.dto.request.ChatRequest;
import me.hal8.sm.chat.dto.response.ChatResponse;
import me.hal8.sm.chat.exception.TooFewParticipantException;
import me.hal8.sm.chat.exception.ResourceNotFoundException;
import me.hal8.sm.chat.exception.TooManyParticipantsException;
import me.hal8.sm.chat.mapper.ChatMapper;
import me.hal8.sm.chat.mapper.ChatParticipantMapper;
import me.hal8.sm.chat.repository.ChatRepository;
import me.hal8.sm.chat.service.IChatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements IChatService {

    private ChatRepository chatRepository;
    private ChatMapper mapper;
    private ChatParticipantMapper participantMapper;

    @Override
    @Transactional
    public ChatResponse createChat(ChatRequest chatDTO, UUID creatorId) {
        var chat = mapper.toEntity(chatDTO);

        var participantDTOs = chatDTO.getParticipants();

        if(participantDTOs.stream().noneMatch(p->p.getParticipantId().equals(creatorId))){
            var creatorDTO = new ChatParticipantRequest(creatorId);
            participantDTOs.add(creatorDTO);
        }


        if (participantDTOs == null || participantDTOs.size() < 2) {
            throw new TooFewParticipantException("You must add at least 2 participants");
        }

        if (participantDTOs.size() > 256) {
            throw new TooManyParticipantsException("Chat cannot have more than 256 users");
        }

        var participants = participantDTOs.stream()
                .map(participantMapper::toEntity)
                .peek(p -> p.setChat(chat))
                .toList();

        chat.setParticipants(participants);

        var saved = chatRepository.save(chat);

        return mapper.toDto(chat);
    }

    public ChatResponse findChatById(UUID id){
        var chat = chatRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException(String.format("no chat with id %s",id));
        });
        return mapper.toDto(chat);
    }

    @Override
    public ChatResponse updateParticipants(ChatRequest chatDTO, List<ChatParticipantRequest> chatParticipantDTOS) {
        var chat = mapper.toEntity(chatDTO);
        var saved = chatRepository.save(chat);
        return mapper.toDto(saved);
    }

}
