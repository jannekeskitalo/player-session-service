package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryItem;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<QueryItem> queryItems = new ArrayList<>();
        queryRepository.getLastSessionsByPlayer(playerId).forEach(record -> {
            queryItems.add(converter.toQueryItem(record));
        });
        return QueryResponse.builder()
                .queryItems(queryItems)
                .build();
    }
}