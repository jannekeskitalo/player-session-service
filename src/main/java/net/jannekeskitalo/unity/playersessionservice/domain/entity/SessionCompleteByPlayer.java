package net.jannekeskitalo.unity.playersessionservice.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Table("session_complete_by_player")
public class SessionCompleteByPlayer {

    @PrimaryKeyColumn(name = "player_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String playerId;

    @PrimaryKeyColumn(name = "session_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
    private UUID sessionId;

    @Column("country")
    private String country;

    @Column("start_ts")
    private LocalDateTime startTs;

    @PrimaryKeyColumn(name = "end_ts", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime endTs;
}