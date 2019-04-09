package net.jannekeskitalo.unity.playersessionservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/ingest")
public interface IngestEventAPI {

    @RequestMapping(method = RequestMethod.POST, path = "")
    ResponseEntity<IngestEventResponse> handleEventBatch(@Valid @RequestBody IngestEventRequest ingestEventRequest);

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    ResponseEntity test(@RequestParam(value = "requestBatchSize", defaultValue = "10") int requestBatchSize,
                        @RequestParam(value = "asyncEnabled", defaultValue = "false") boolean asyncEnabled);
}

