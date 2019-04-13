package net.jannekeskitalo.unity.playersessionservice.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping(path = "/ingest", produces = MediaType.APPLICATION_JSON_VALUE)
public interface IngestEventAPI {

    @RequestMapping(method = RequestMethod.POST, path = "")
    @ApiOperation(value = "Submit a batch of session events")
    ResponseEntity<IngestEventResponse> handleEventBatch(@Valid @RequestBody IngestEventRequest ingestEventRequest);

    @RequestMapping(method=RequestMethod.POST, path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "Upload test data")
    public ResponseEntity<String> ingestFile(
            @ApiParam(required = true, name = "file", value = "File containing example events as json lines")
            @RequestPart MultipartFile file,
            @ApiParam(required = true, name = "count", value = "How many records to load from file")
            @RequestParam(name = "count") int count);

    @RequestMapping(method=RequestMethod.POST, path = "/generate", produces = MediaType.TEXT_PLAIN_VALUE)
    @ApiOperation(value = "Generate test data")
    public ResponseEntity<String> generate(
            //@ApiParam(required = true, name = "count", value = "Count of records") -- this doesn't work. Looks like a swagger bug.
            @RequestParam(name = "count") int count);
}

