package net.jannekeskitalo.unity.playersessionservice.querying;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryAPI;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestHelper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "Querying", description = "Ingests session event batches. Max batch size is 10.")
@RequestMapping(path = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryController implements QueryAPI {

    private final QueryService queryService;
    private final TestHelper testHelper = new TestHelper();

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}")
    public ResponseEntity<QueryResponse> sessionsByPlayer(@PathVariable(name = "playerId") String playerId) {

        log.info("player_id: {}", playerId);
        testHelper.startTimer();
        QueryResponse response = queryService.getSessionsByPlayer(playerId);
        log.info("Elapsed ms: {}", testHelper.stopTimer() / 1000000);
        return ResponseEntity.ok().body(response);
    }

}
