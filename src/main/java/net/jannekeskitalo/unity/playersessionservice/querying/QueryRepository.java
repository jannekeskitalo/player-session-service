package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.concurrent.ListenableFuture;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Repository
public class QueryRepository {

    private final AsyncCassandraTemplate asyncCassandraTemplate;
    private final CassandraTemplate cassandraTemplate;

    @Autowired
    public QueryRepository(AsyncCassandraTemplate asyncCassandraTemplate, CassandraTemplate cassandraTemplate) {
        this.asyncCassandraTemplate = asyncCassandraTemplate;
        this.cassandraTemplate = cassandraTemplate;
    }

    public List<SessionByPlayerId> getLastSessionsByPlayer(String playerId) {
        return cassandraTemplate
                .select(query(where("player_id").is(playerId))
                .and(where("ts").gt(LocalDateTime.parse("1970-01-01T00:00:00")))
                        .sort(Sort.by("ts").descending()).limit(1000), SessionByPlayerId.class);
    }

    public ListenableFuture getLastStartedSessionsByCountryForBucket(String country, LocalDateTime hour, int bucket, Consumer consumer) {
        Query q = query(where("country").is(country))
                .and(where("bucket").is(bucket))
                .and(where("start_hour_ts").is(hour))
                .sort(Sort.by("start_ts").descending());
        return asyncCassandraTemplate.select(q, consumer, SessionStartedByCountry.class);
    }
}
