package net.jannekeskitalo.unity.playersessionservice.querying;

import jnr.ffi.Struct;
import jnr.ffi.annotations.Out;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryItem;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.jannekeskitalo.unity.playersessionservice.ingestion.Bucketizer.BUCKET_COUNT;


@Slf4j
@Service
public class QueryService {

    private final QueryRepository queryRepository;

    public QueryService(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
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

    public void getSessionsByCountry(String country, LocalDateTime hour, ResponseBodyEmitter emitter) {

        ListenableFuture f;
        for (int bucket = 1; bucket <=  BUCKET_COUNT; bucket++) {
            f = queryRepository.getLastStartedSessionsByCountryForBucket(country, hour, bucket, new ResultConsumer(emitter));
            try { f.get(); } catch (Exception e) { log.error("Error reported fetching bucket: {}", e); }
        }
    }
}