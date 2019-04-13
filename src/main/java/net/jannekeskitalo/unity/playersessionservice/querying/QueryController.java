package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryAPI;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestHelper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import java.io.IOException;


@Slf4j
@RestController
@RequestMapping(path = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class QueryController implements QueryAPI {

    private final QueryService queryService;
    private final TestHelper testHelper = new TestHelper();

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> sessionsByPlayer(@PathVariable(name = "playerId") String playerId) {

        log.info("player_id: {}", playerId);
        testHelper.startTimer();
        QueryResponse response = queryService.getSessionsByPlayer(playerId);
        log.info("Elapsed ms: {}", testHelper.stopTimer() / 1000000);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/by-country/{country}/{hours}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> sessionsByCountry(
            @PathVariable(name = "country") String country,
            @PathVariable(name = "hours") int hours) throws IOException {
        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        queryService.getSessionsByCountry(country, hours, emitter);
        emitter.complete();
        return ResponseEntity.ok(emitter);
    }
}
