package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import static org.springframework.data.cassandra.core.query.Criteria.where;
import static org.springframework.data.cassandra.core.query.Query.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

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

    // Couldn't find a traditional iterator select from spring cassandra library,
    // so putting a hard limit of 1000 rows here.
    // If the events are valid and "end" always follows "start", then this should work with a limit of 40.
    public List<SessionByPlayerId> getLastSessionsByPlayer(String playerId) {
        return cassandraTemplate
                .select(query(where("player_id").is(playerId))
                .and(where("ts").gt(LocalDateTime.parse("1970-01-01T00:00:00")))
                        .sort(Sort.by("ts").descending()).limit(1000), SessionByPlayerId.class);
    }
}
