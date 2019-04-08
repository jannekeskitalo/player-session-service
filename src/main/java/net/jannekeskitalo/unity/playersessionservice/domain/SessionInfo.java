package net.jannekeskitalo.unity.playersessionservice.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Table("session_info")
public class SessionInfo {

    @PrimaryKeyColumn(name = "session_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID sessionId;

    @Column("player_id")
    private UUID playerId;

    @Column("started_ts")
    private LocalDateTime startedTs;

    @Column("ended_ts")
    private LocalDateTime endedTs;

    @Column("country")
    private String country;
}

