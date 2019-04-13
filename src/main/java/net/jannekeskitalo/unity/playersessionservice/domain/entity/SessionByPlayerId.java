package net.jannekeskitalo.unity.playersessionservice.domain.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@Table("session_by_player_id")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SessionByPlayerId {

    @PrimaryKeyColumn(name = "player_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String playerId;

    @Column("session_id")
    private UUID sessionId;

    @Column("event")
    private String event;

    @Column("country")
    private String country;

    @PrimaryKeyColumn(name = "ts", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime ts;

}