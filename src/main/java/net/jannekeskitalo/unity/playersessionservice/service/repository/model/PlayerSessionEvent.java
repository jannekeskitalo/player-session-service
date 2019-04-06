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
public class PlayerSessionEvent {

    @PrimaryKeyColumn(name = "player_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private UUID playerId;

    @PrimaryKeyColumn(name = "session_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID sessionId;

    @PrimaryKeyColumn(name = "country", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int countryId;


    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucketId;



    @Column("ts")
    private LocalDateTime ts;
}

