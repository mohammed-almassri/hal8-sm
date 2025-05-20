package me.hal8.sm.chat.repository;

import me.hal8.sm.chat.entity.Chat;
import me.hal8.sm.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> getChatByParticipants(List<ChatParticipant> participants);
}
