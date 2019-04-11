package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryItem;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;


@Slf4j
@Service
public class QueryService {

    private final QueryRepository queryRepository;
    private final QueryResponseConverter converter;

    public QueryService(QueryRepository queryRepository, QueryResponseConverter converter) {
        this.queryRepository = queryRepository;
        this.converter = converter;
    }

    public QueryResponse getSessionsByPlayer(String playerId) {
        List<SessionByPlayerId> eventResults = queryRepository.getLastSessionsByPlayer(playerId);
        log.info("Found {} session events for player {}", eventResults.size(), playerId);
        QueryResponse queryResponse = null;
        Map<UUID,QueryItem> sessions = new HashMap<>(20);
        int completeSessionCount = 0;
        QueryItem itemAtHand;
        for (SessionByPlayerId event : eventResults) {
            if (event.getEvent().equals("end")) {
                itemAtHand = QueryItem.builder()
                        .sessionId(event.getSessionId())
                        .playerId(event.getPlayerId())
                        .country(null)
                        .startTs(null)
                        .endTs(event.getTs())
                        .build();
                sessions.put(itemAtHand.getSessionId(), itemAtHand);
            } else {
                itemAtHand = sessions.get(event.getSessionId());
                if (itemAtHand != null && event.getEvent().equals("start")) {
                    itemAtHand.setCountry(event.getCountry());
                    itemAtHand.setStartTs(event.getTs());
                    completeSessionCount++;
                }
            }
            if (completeSessionCount == 20) {
                queryResponse = QueryResponse.builder().queryItems(new ArrayList<QueryItem>(sessions.values())).build();
                break;
            }
        }
        if (queryResponse == null) {
            queryResponse = QueryResponse.builder().queryItems(new ArrayList<QueryItem>(sessions.values())).build();
        }
        return queryResponse;
    }
}