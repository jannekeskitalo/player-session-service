package net.jannekeskitalo.unity.playersessionservice.ingestion;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventAPI;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Api(tags = "Ingestion", description = "Ingests session event batches. Max batch size is 10.")
@RequestMapping(path = "/ingest", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngestEventController implements IngestEventAPI {

    private final IngestService ingestService;
    private final IngestFileService ingestFileService;
    private final TestHelper testHelper = new TestHelper();

    @Autowired
    public IngestEventController(IngestService ingestService, IngestFileService ingestFileService) {
        this.ingestService = ingestService;
        this.ingestFileService = ingestFileService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<IngestEventResponse> handleEventBatch(@Valid @RequestBody IngestEventRequest request) {

        testHelper.startTimer();
        ingestService.handleEventBatchAsync(request);
        long timeElapsed = testHelper.stopTimer();
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

    @RequestMapping(method=RequestMethod.POST, path = "/test/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> ingestFile(@RequestPart MultipartFile file) {
        testHelper.startTimer();
        ingestFileService.handleFile(file);
        log.info("Elapsed seconds: {}", testHelper.stopTimer() / 1000000000);
        return ResponseEntity.accepted().body("File ingested");
    }

}
