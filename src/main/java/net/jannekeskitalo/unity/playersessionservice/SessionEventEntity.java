package net.jannekeskitalo.unity.playersessionservice;

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
public class SessionEventEntity {
    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int countryId;

    @PrimaryKeyColumn(name = "hour", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private int epochHour;

    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucketId;

    @PrimaryKeyColumn(name = "session_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID sessionId;

    @Column("player_id")
    private UUID playerId;

    @Column("ts")
    private LocalDateTime ts;
}

