package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventAPI;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventResponse;
import net.jannekeskitalo.unity.playersessionservice.util.TestDataHelper;
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
@RequestMapping(path = "/ingest", produces = MediaType.APPLICATION_JSON_VALUE)
public class IngestEventController implements IngestEventAPI {

    private final IngestService ingestService;
    private final IngestFileService ingestFileService;
    TestDataHelper testDataHelper;

    @Autowired
    public IngestEventController(IngestService ingestService, IngestFileService ingestFileService, TestDataHelper testDataHelper) {
        this.ingestService = ingestService;
        this.ingestFileService = ingestFileService;
        this.testDataHelper = testDataHelper;
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<IngestEventResponse> handleEventBatch(@Valid @RequestBody IngestEventRequest request) {

        final TestHelper testHelper = TestHelper.startiming();
        ingestService.handleEventBatchSync(request);
        long timeElapsed = testHelper.stopTimer();
        log.info("Elapsed micros: {}, micros per event: {}", timeElapsed / 1000, timeElapsed / request.getEventBatch().size() / 1000);

        return ResponseEntity.ok(IngestEventResponse.builder().ingestedEventCount(request.getEventBatch().size()).ts(LocalDateTime.now()).build());
    }

    @RequestMapping(method=RequestMethod.POST, path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> ingestFile(@RequestPart MultipartFile file, @RequestParam(name = "count") int count) {

        TestHelper testHelper = TestHelper.startiming();
        ingestFileService.handleFile(file, count);
        log.info("Elapsed seconds: {}", testHelper.stopTimer() / 1000000000);
        return ResponseEntity.accepted().body("File ingested");
    }

    @RequestMapping(method=RequestMethod.POST, path = "/generate")
    public ResponseEntity<String> generate(@RequestParam(name = "count") int count) {

        TestHelper testHelper = TestHelper.startiming();
        testDataHelper.generateTestData(count);
        log.info("Elapsed seconds: {}", testHelper.stopTimer() / 1000000000);
        return ResponseEntity.accepted().contentType(MediaType.TEXT_PLAIN).body("Generated " + count + " records");
    }

}
