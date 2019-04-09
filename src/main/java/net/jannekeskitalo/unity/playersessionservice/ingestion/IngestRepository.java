package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionInfo;
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

    public List<CompletableFuture<SessionInfo>> saveBatchAsync(Iterable<SessionInfo> sessionInfos) {
        List<CompletableFuture<SessionInfo>> futures = new ArrayList<>();
        for (SessionInfo record: sessionInfos) {
            futures.add(asyncCassandraTemplate.insert(record).completable());
        }
        return futures;
    }
}
