package me.hal8.sm.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.UUID;

@Entity
public class Booking {
    @Id
    @Column(updatable = false, nullable = false, columnDefinition = "UUID")
    private long id;

    private int room;
    private Instant timeFrom;
    private Instant timeTo;
}
