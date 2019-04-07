package net.jannekeskitalo.unity.playersessionservice.service;

import net.jannekeskitalo.unity.playersessionservice.api.NewSessionEventRequest;
import net.jannekeskitalo.unity.playersessionservice.service.model.SessionInfo;
import net.jannekeskitalo.unity.playersessionservice.service.repository.SessionInfoRepository;
import net.jannekeskitalo.unity.playersessionservice.service.repository.SessionInfoRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionInfoRepositoryCustom sessionInfoRepository;

    public SessionService(@Autowired SessionInfoRepositoryCustom sessionInfoRepository) {
        this.sessionInfoRepository = sessionInfoRepository;
    }

    public void handleNewSessionEvent(List<NewSessionEventRequest> requests) {

        List<SessionInfo> records = new ArrayList<>();
        for (NewSessionEventRequest request : requests) {
            records.add(SessionInfo.builder()
                    .sessionId(request.getSessionId())
                    .playerId(request.getPlayerId())
                    .startedTs(request.getTs())
                    .country(request.getCountry())
                    .build());

            sessionInfoRepository.saveBatch(records);
        }
    }
}
