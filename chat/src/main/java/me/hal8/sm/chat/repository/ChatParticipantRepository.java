package me.hal8.sm.chat.repository;

import me.hal8.sm.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {
}
