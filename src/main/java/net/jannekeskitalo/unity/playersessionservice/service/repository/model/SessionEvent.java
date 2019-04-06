package net.jannekeskitalo.unity.playersessionservice.service.repository.model;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table
@Data
public class SessionEvent {

    @Column("player_id")
    private UUID playerId;

    @Column("ts")
    private LocalDateTime ts;
}
