package me.hal8.sm.chat.mapper;

import me.hal8.sm.chat.dto.request.ChatRequest;
import me.hal8.sm.chat.dto.response.ChatResponse;
import me.hal8.sm.chat.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target = "messages", source = "messages")
    ChatResponse toDto(Chat chat);

    Chat toEntity(ChatRequest chatDTO);

}