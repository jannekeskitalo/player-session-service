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
@Table("session_by_id")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SessionById {

    @PrimaryKeyColumn(name = "session_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID sessionId;

    @Column("player_id")
    private String playerId;

    @Column("start_ts")
    private LocalDateTime startTs;

    @Column("end_ts")
    private LocalDateTime endTs;

    @PrimaryKeyColumn(name = "start_hour", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime startHourTs;

    @Column("end_hour")
    private LocalDateTime endHourTs;

    @Column("country")
    private String country;
}

