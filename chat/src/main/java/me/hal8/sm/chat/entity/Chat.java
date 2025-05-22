package me.hal8.sm.chat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.hal8.sm.chat.enums.ChatType;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @OneToMany(mappedBy = "chat",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<ChatParticipant> participants;

    private String name;
    private UUID createdBy;

    @OneToMany(mappedBy = "chat",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Message> messages;
}
