package net.jannekeskitalo.unity.playersessionservice.ingest;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.domain.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.domain.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.IngestEventAPI;
import net.jannekeskitalo.unity.playersessionservice.domain.IngestEventResponse;
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

    public IngestEventController(@Autowired IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<IngestEventResponse> handleEventBatch(
            @RequestParam(value = "count", defaultValue = "1") int count,
            @Valid @RequestBody IngestEventRequest request) {

        long start = System.nanoTime();

        ingestService.handleEventBatch(request);

        long finish = System.nanoTime();
        long timeElapsed = finish - start;

        log.info("Elapsed micros: {}, micros per event: {}", timeElapsed / 1000, timeElapsed / count / 1000);

        return ResponseEntity.ok(IngestEventResponse.builder().ingestedEventCount(request.getEventBatch().size()).ts(LocalDateTime.now()).build());
    }
}
