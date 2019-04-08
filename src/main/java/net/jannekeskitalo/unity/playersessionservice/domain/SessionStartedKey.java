package net.jannekeskitalo.unity.playersessionservice.domain;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@Data
@PrimaryKeyClass
public class SessionStartedKey implements Serializable {

    @PrimaryKeyColumn(name = "country_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int countryId;

    @PrimaryKeyColumn(name = "hour", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private int hour;

    @PrimaryKeyColumn(name = "bucket", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private int bucket;

    @PrimaryKeyColumn(name = "session_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID sessionId;

}
