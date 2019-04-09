package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionCompleteByPlayer;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.AsyncCassandraTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    public CompletableFuture<SessionCompleteByPlayer> insertSessionCompleteByPlayer(SessionCompleteByPlayer record) {
        return asyncCassandraTemplate.insert(record).completable();
    }
}
