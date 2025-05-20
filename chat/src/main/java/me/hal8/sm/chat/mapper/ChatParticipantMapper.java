package me.hal8.sm.chat.mapper;

import me.hal8.sm.chat.dto.request.ChatParticipantRequest;
import me.hal8.sm.chat.dto.response.ChatParticipantResponse;
import me.hal8.sm.chat.entity.ChatParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatParticipantMapper {
    ChatParticipantMapper INSTANCE = Mappers.getMapper(ChatParticipantMapper.class);

    ChatParticipantResponse toDto(ChatParticipant chat);
    ChatParticipant toEntity(ChatParticipantRequest chatDTO);
}
