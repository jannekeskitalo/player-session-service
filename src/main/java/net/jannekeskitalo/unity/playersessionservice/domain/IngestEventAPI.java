package net.jannekeskitalo.unity.playersessionservice.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/ingest")
public interface IngestEventAPI {

    @RequestMapping(method = RequestMethod.POST, path = "")
    ResponseEntity<IngestEventResponse> handleEventBatch(
            @RequestParam(value = "count", defaultValue = "1") int count,
            @Valid @RequestBody IngestEventRequest ingestEventRequest);
}

