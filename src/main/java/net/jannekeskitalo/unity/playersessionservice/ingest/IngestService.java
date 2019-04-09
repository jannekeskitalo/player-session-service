package net.jannekeskitalo.unity.playersessionservice.ingest;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.domain.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class IngestService {

    private final SessionInfoRepositoryCustom sessionInfoRepository;

    public IngestService(@Autowired SessionInfoRepositoryCustom sessionInfoRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
    }

    public void handleEventBatchAsync(IngestEventRequest request) {
        handleEventBatch(request, true);
    }

    public void handleEventBatchSync(IngestEventRequest request) {
        handleEventBatch(request, false);
    }

    public void handleEventBatch(IngestEventRequest request, boolean asyncEnabled) {

        List<SessionInfo> records = new ArrayList<>();
        for (IngestEvent event : request.getEventBatch()) {

            records.add(SessionInfo.builder()
                    .sessionId(event.getSessionId())
                    .playerId(event.getPlayerId())
                    .startTs(event.getTs())
                    .endTs(event.getTs())
                    .startHourTs(event.getTs().truncatedTo(ChronoUnit.HOURS))
                    .endHourTs(event.getTs().truncatedTo(ChronoUnit.HOURS))
                    .country(event.getCountry())
                    .build());

            if (asyncEnabled) {
                sessionInfoRepository.saveBatchAsync(records);
            } else {
                waitAllToComplete(sessionInfoRepository.saveBatchAsync(records));
            }
        }
    }

    private void waitAllToComplete(List<CompletableFuture<SessionInfo>> futures) {
        futures.forEach(CompletableFuture::join);
    }
}
