package net.jannekeskitalo.unity.playersessionservice.querying;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.QueryAPI;
import net.jannekeskitalo.unity.playersessionservice.api.QueryItem;
import net.jannekeskitalo.unity.playersessionservice.api.QueryResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestHelper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> sessionsByPlayer(@PathVariable(name = "playerId") String playerId) {

        log.info("player_id: {}", playerId);
        testHelper.startTimer();
        QueryResponse response = queryService.getSessionsByPlayer(playerId);
        log.info("Elapsed ms: {}", testHelper.stopTimer() / 1000000);
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/by-country/{country}/{hour}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyEmitter> sessionsByCountry(
            @PathVariable(name = "country") String country,
            @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
            @PathVariable(name = "hour") LocalDateTime hour) throws IOException {
        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        queryService.getSessionsByCountry(country, hour, emitter);
        emitter.complete();
        return ResponseEntity.ok(emitter);
    }
}
