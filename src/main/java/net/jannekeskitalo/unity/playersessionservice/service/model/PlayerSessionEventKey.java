package net.jannekeskitalo.unity.playersessionservice.service.model;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@PrimaryKeyClass
public class PlayerSessionEventKey implements Serializable {

    @PrimaryKeyColumn(name = "player_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int playerId;

    @PrimaryKeyColumn(name = "ts", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private LocalDateTime ts;

    @PrimaryKeyColumn(name = "session_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID sessionId;

}