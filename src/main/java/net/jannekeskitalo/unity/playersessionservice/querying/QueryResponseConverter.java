package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryItem;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueryResponseConverter {

    public QueryItem toQueryItem(SessionByPlayerId record) {
        return QueryItem.builder()
                .playerId(record.getPlayerId())
                .sessionId(record.getSessionId())
                .country(record.getCountry())
                .startTs(record.getTs())
                .endTs(record.getTs())
                .build();
    }
}
