package net.jannekeskitalo.unity.playersessionservice.domain.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("session_started")
public class SessionStarted {

    @PrimaryKeyColumn(name = "session_id", type = PrimaryKeyType.PARTITIONED)
    private UUID session_id;

    @Column("player_id")
    private UUID playerId;

    @Column("started_ts")
    private LocalDateTime startedTs;

    @Column("ended_ts")
    private LocalDateTime endedTs;

    @Column("country")
    private String country;
}

