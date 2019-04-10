package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionCompleteByPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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

    public List<SessionCompleteByPlayer> getLastSessionsByPlayer(String player_id) {
        String cql = "select * from session_complete_by_player where player_id = '" + player_id + "' order by end_ts desc limit 20";
        return cassandraTemplate.select(cql, SessionCompleteByPlayer.class);
    }
}
