package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.data.cassandra.core.query.Query;
import org.springframework.data.cassandra.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

import static org.springframework.data.cassandra.core.query.Criteria.where;

@Slf4j
@Repository
public class IngestRepository {

    private final AsyncCassandraTemplate asyncCassandraTemplate;

    public IngestRepository(@Autowired AsyncCassandraTemplate asyncCassandraTemplate) {
        this.asyncCassandraTemplate = asyncCassandraTemplate;
    }

    public CompletableFuture<SessionById> insertSessionById(SessionById record) {
        return asyncCassandraTemplate.insert(record).completable();
    }

    public CompletableFuture<SessionStartedByCountry> insertSessionStartedByCountry(SessionStartedByCountry record) {
        return asyncCassandraTemplate.insert(record).completable();
    }

    public CompletableFuture<SessionByPlayerId> insertSessionByPlayer(SessionByPlayerId record) {
        return asyncCassandraTemplate.insert(record).completable();
    }

/*    // NOT WORKING
    public CompletableFuture<Boolean> updateSessionCompleteByPlayer(SessionByPlayerId record) {
        Query query = Query
                .query(where("playerId").is(record.getPlayerId()))
                .and(where("startTs").is())
                .and(where("sessionId").is(record.getSessionId()));
        Update update = Update.empty().set("endTs", record.getEndTs());
        log.info("Updating: {}", record);
        log.info("Query: {}", query);
        return asyncCassandraTemplate.update(query, update, SessionByPlayerId.class).completable();
    }*/
}
