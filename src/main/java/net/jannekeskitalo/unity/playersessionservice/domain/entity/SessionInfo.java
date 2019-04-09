package net.jannekeskitalo.unity.playersessionservice.domain.entity;

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

    @Column("start_ts")
    private LocalDateTime startTs;

    @Column("end_ts")
    private LocalDateTime endTs;

    @Column("start_hour")
    private LocalDateTime startHourTs;

    @Column("end_hour")
    private LocalDateTime endHourTs;

    @Column("country")
    private String country;
}

