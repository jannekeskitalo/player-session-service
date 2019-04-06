package net.jannekeskitalo.unity.playersessionservice.service.model;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SessionEvent {

    @Column("player_id")
    private UUID playerId;

    @Column("ts")
    private LocalDateTime ts;
}

