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
@Table("session_started_by_country")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SessionStartedByCountry {

    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String country;

    @PrimaryKeyColumn(name = "start_hour_ts", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private LocalDateTime startHourTs;

    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucket;

    @PrimaryKeyColumn(name = "start_ts", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime startTs;

    @PrimaryKeyColumn(name = "session_id", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
    private UUID sessionId;

    @Column("player_id")
    private String playerId;


}

