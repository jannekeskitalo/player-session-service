package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionCompleteByPlayer;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
            handleSessionById(request.getEventBatch());
            handleSessionStartedByCountry(request.getEventBatch());
            handleSessionCompleteByPlayer(request.getEventBatch());
        } else {
            waitAllToComplete(handleSessionById(request.getEventBatch()));
            waitAllToComplete(handleSessionStartedByCountry(request.getEventBatch()));
            waitAllToComplete(handleSessionCompleteByPlayer(request.getEventBatch()));
        }
    }

    private List<CompletableFuture<SessionById>> handleSessionById(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionById>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            futures.add(ingestRepository.insertSessionById(entityConverter.toSessionById(event)));
        }
        return futures;
    }

    private List<CompletableFuture<SessionStartedByCountry>> handleSessionStartedByCountry(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionStartedByCountry>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            futures.add(ingestRepository.insertSessionStartedByCountry(entityConverter.toSessionStartedByCountry(event)));
        }
        return futures;
    }

    private List<CompletableFuture<SessionCompleteByPlayer>> handleSessionCompleteByPlayer(List<IngestEvent> ingestEventList) {
        List<CompletableFuture<SessionCompleteByPlayer>> futures = new ArrayList<>();
        for (IngestEvent event : ingestEventList) {
            futures.add(ingestRepository.insertSessionCompleteByPlayer(entityConverter.toSessionCompleteByPlayer(event)));
        }
        return futures;
    }

    private <T> void waitAllToComplete(List<CompletableFuture<T>> futures) {
        futures.forEach(CompletableFuture::join);
    }
}
