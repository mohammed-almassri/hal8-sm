package me.hal8.sm.chat.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.hal8.sm.chat.dto.request.MessageRequest;
import me.hal8.sm.chat.dto.response.MessageResponse;
import me.hal8.sm.chat.exception.ResourceNotFoundException;
import me.hal8.sm.chat.mapper.MessageMapper;
import me.hal8.sm.chat.repository.ChatRepository;
import me.hal8.sm.chat.repository.MessageRepository;
import me.hal8.sm.chat.service.IMessageService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private MessageRepository messageRepository;
    private ChatRepository chatRepository;
    private MessageMapper mapper;

    @Override
    @Transactional
    public MessageResponse createMessage(MessageRequest messageDTO,UUID userId) {
        var chat = chatRepository.findById(messageDTO.getChatId()).orElseThrow(()->{
            throw new ResourceNotFoundException("no chat with id "+messageDTO.getChatId());
        });
        messageDTO.setChatId(chat.getId());
        var message = mapper.toEntity(messageDTO);
        message.setSenderId(userId);
        var saved = messageRepository.save(message);
        return mapper.toDto(saved);
    }

    public MessageResponse findMessageById(UUID id){
        var message = messageRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException(String.format("no message with id %s",id));
        });
        return mapper.toDto(message);
    }

}
