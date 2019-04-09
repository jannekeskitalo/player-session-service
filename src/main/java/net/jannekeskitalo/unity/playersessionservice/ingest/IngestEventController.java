package net.jannekeskitalo.unity.playersessionservice.ingest;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.api.IngestEventAPI;
import net.jannekeskitalo.unity.playersessionservice.domain.api.IngestEventResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "Ingestion", description = "Ingests session event batches. Max batch size is 10.")
@RequestMapping(path = "/ingest", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngestEventController implements IngestEventAPI {

    private final IngestService ingestService;
    private final TestHelper testHelper = new TestHelper();

    public IngestEventController(@Autowired IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<IngestEventResponse> handleEventBatch(
            @Valid @RequestBody IngestEventRequest request) {

        long start = System.nanoTime();

        ingestService.handleEventBatchSync(request);

        long finish = System.nanoTime();
        long timeElapsed = finish - start;

        log.info("Elapsed micros: {}, micros per event: {}", timeElapsed / 1000, timeElapsed / request.getEventBatch().size() / 1000);

        return ResponseEntity.ok(IngestEventResponse.builder().ingestedEventCount(request.getEventBatch().size()).ts(LocalDateTime.now()).build());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public ResponseEntity test(@RequestParam(value = "requestBatchSize", defaultValue = "10") int requestBatchSize,
                               @RequestParam(value = "asyncEnabled", defaultValue = "false") boolean asyncEnabled) {

        IngestEventRequest ingestEventRequest = TestHelper.createIngestEventRequestOfSize(requestBatchSize);
        testHelper.startTimer();
        if (asyncEnabled) {
            ingestService.handleEventBatchAsync(ingestEventRequest);
        } else {
            ingestService.handleEventBatchSync(ingestEventRequest);
        }
        log.info("Elapsed per event: {}", testHelper.stopTimer() / ingestEventRequest.getEventBatch().size() / 1000000);
        return ResponseEntity.accepted().build();
    }

}
