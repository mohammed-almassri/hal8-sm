package me.hal8.sm.chat.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
   @Id
   @Column(updatable = false,nullable = false,columnDefinition = "UUID")
   @Setter
   private UUID id;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Instant deletedAt;

    @PrePersist
    protected void onCreate(){
        if(id==null){
            this.id = UuidCreator.getTimeOrderedEpoch();
        }
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void softDelete() {
        this.deletedAt = Instant.now();
    }
}
