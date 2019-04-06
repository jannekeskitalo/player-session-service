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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        log.info("Get list of entities");


        List<SessionEventResponse> response = new ArrayList<SessionEventResponse>();
        IntStream.range(1, 10).forEach(event -> {
                    response.add(SessionEventResponse.builder()
                            .event("start")
                            .country("FI")
                            .playerId(UUID.randomUUID())
                            .sessionId(UUID.randomUUID())
                            .ts(LocalDateTime.now())
                            .build());
                });

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
                    CreateSessionEventRequest request) {

        SessionEventResponse sessionEventResponse = SessionEventResponse.builder()
                .event(request.getEvent())
                .country(request.getCountry())
                .playerId(request.getPlayerId())
                .sessionId(request.getSessionId())
                .ts(request.getTs())
                .build();

        log.info("Created event {}", sessionEventResponse);
        return ResponseEntity.ok(sessionEventResponse);
    }
}
