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
@Table("session_started_by_country")
public class SessionStartedByCountry {

    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "start_hour_ts", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private LocalDateTime startHourTs;

    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucket;

    @PrimaryKeyColumn(name = "session_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID sessionId;

    @Column("player_id")
    private UUID playerId;

    @Column("start_ts")
    private LocalDateTime startTs;

    @Column("end_ts")
    private LocalDateTime endTs;

}

