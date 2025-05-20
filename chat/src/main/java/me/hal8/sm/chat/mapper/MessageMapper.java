package me.hal8.sm.chat.mapper;

import me.hal8.sm.chat.dto.request.MessageRequest;
import me.hal8.sm.chat.dto.response.MessageResponse;
import me.hal8.sm.chat.entity.Chat;
import me.hal8.sm.chat.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "chatId", target = "chat")
    public abstract Message toEntity(MessageRequest messageRequest);
    @Mapping(source = "chat.id", target = "chatId")
    public abstract MessageResponse toDto(Message message);

    default Chat mapChatId(UUID chatId) {
        if (chatId == null) {
            return null;
        }
        var c = new Chat();
        c.setId(chatId);
        return c;
    }

//    public static MessageDTO mapToDTO(Message message) {
//        MessageDTO dto = new MessageDTO();
//        dto.setId(message.getId());
//        dto.setSenderId(message.getSenderId());
//        dto.setContent(message.getContent());
//        dto.setChatId(message.getChat() != null ? message.getChat().getId() : null);
//        return dto;
//    }
//
//    public static Message mapToEntity(MessageDTO dto, Chat chat) {
//        Message message = new Message();
//        message.setChat(chat);
//        message.setSenderId(dto.getSenderId());
//        message.setContent(dto.getContent());
//        return message;
//    }
}
