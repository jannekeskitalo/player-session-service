package net.jannekeskitalo.unity.playersessionservice.ingest;

import net.jannekeskitalo.unity.playersessionservice.domain.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.domain.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngestService {

    private final SessionInfoRepositoryCustom sessionInfoRepository;

    public IngestService(@Autowired SessionInfoRepositoryCustom sessionInfoRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
    }

    public void handleEventBatch(IngestEventRequest request) {

        List<SessionInfo> records = new ArrayList<>();
        for (IngestEvent event : request.getEventBatch()) {

            records.add(SessionInfo.builder()
                    .sessionId(event.getSessionId())
                    .playerId(event.getPlayerId())
                    .startedTs(event.getTs())
                    .country(event.getCountry())
                    .build());

            sessionInfoRepository.saveBatch(records);
        }
    }
}
