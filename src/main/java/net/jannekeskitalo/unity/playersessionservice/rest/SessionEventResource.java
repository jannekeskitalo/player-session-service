package net.jannekeskitalo.unity.playersessionservice.rest;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.CreateSessionEventRequest;
import net.jannekeskitalo.unity.playersessionservice.api.SessionEventResourceAPI;
import net.jannekeskitalo.unity.playersessionservice.api.SessionEventResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "Session Event entity", description = "Consumes session events and provides metrics")
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionEventResource implements SessionEventResourceAPI {

    @RequestMapping(method = RequestMethod.GET, path = "{country}")
    @ApiOperation(value = "List events for last n hours", notes = "Returns a list of events for the last n hours for a country")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No content")})
    public ResponseEntity<List<SessionEventResponse>> getEventsForCountry(
            @PathVariable("country") String country,
            @RequestParam(value = "hours", defaultValue = "1")
            @ApiParam(name = "hours",
                    value = "For how many hours back to get events",
                    example = "3",
                    required = true)
                    int hours)
    {
        log.debug("Get list of entities");

        SessionEventResponse sessionEventResponse = SessionEventResponse.builder()
                .event("start")
                .country("FI")
                .playerId(UUID.randomUUID())
                .sessionId(UUID.randomUUID())
                .ts(LocalDateTime.now())
                .build();

        List<SessionEventResponse> response = Collections.singletonList(sessionEventResponse);
        if (response.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.POST, path = "")
    @ApiOperation(value = "Create event", notes = "Create a session event")
    public ResponseEntity<SessionEventResponse> createSessionEvent(
            @Valid @RequestBody
            @ApiParam(name = "request",
                    value = "Create event request<br>Refer to model for detailed documentation",
                    required = true)
                    CreateSessionEventRequest createSessionEventRequest) {

        SessionEventResponse sessionEventResponse = SessionEventResponse.builder()
                .event("start")
                .country("FI")
                .playerId(UUID.randomUUID())
                .sessionId(UUID.randomUUID())
                .ts(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(sessionEventResponse);
    }
}
