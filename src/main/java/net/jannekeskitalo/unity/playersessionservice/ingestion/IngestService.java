package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Transforms ingested events and inserts/updates into corresponding Cassandra tables.
 * Uses asynchronous sending internally, but still offers synchronous semantics for API client.
 * Design assumes the client will resend the event batch if there are errors in cassandra. Atomic
 * batch operation is not used, because it's slow when touching multiple partitions. The chosen
 * approach is eventually consistent IF the client does retries as expected.
 */
@Slf4j
@Service
public class IngestService {

    private final IngestRepository ingestRepository;
    private final EntityConverter entityConverter;

    @Autowired
    public IngestService(IngestRepository sessionInfoRepository, EntityConverter entityConverter) {
        this.ingestRepository = sessionInfoRepository;
        this.entityConverter = entityConverter;
    }

    public void handleEventBatchAsync(IngestEventRequest request) {
        handleEventBatch(request, true);
    }

    public void handleEventBatchSync(IngestEventRequest request) {
        handleEventBatch(request, false);
    }

    public void handleEventBatch(IngestEventRequest request, boolean asyncEnabled) {

        if (asyncEnabled) {
            //handleSessionById(request.getEventBatch());
            handleSessionStartedByCountry(request.getEventBatch());
            handleSessionByPlayer(request.getEventBatch());
        } else {
            //waitAllToComplete(handleSessionById(request.getEventBatch()));
            waitAllToComplete(handleSessionStartedByCountry(request.getEventBatch()));
            waitAllToComplete(handleSessionByPlayer(request.getEventBatch()));
        }
    }

    private List<CompletableFuture<SessionById>> handleSessionById(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionById>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            entityConverter.toSessionById(event).ifPresent(e -> futures.add(ingestRepository.insertSessionById(e)));
        }
        return futures;
    }

    private List<CompletableFuture<SessionStartedByCountry>> handleSessionStartedByCountry(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionStartedByCountry>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            entityConverter.toSessionStartedByCountry(event).ifPresent(e -> futures.add(ingestRepository.insertSessionStartedByCountry(e)));
        }
        return futures;
    }

    private List<CompletableFuture<SessionByPlayerId>> handleSessionByPlayer(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionByPlayerId>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            entityConverter.toSessionByPlayer(event).ifPresent(e -> futures.add(ingestRepository.insertSessionByPlayer(e)));
        }
        return futures;
    }

    private <T> void waitAllToComplete(List<CompletableFuture<T>> futures) {
        futures.forEach(CompletableFuture::join);
    }
}
