package net.jannekeskitalo.unity.playersessionservice.rest;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.NewSessionEventRequest;
import net.jannekeskitalo.unity.playersessionservice.api.SessionEventResourceAPI;
import net.jannekeskitalo.unity.playersessionservice.api.SessionEventResponse;
import net.jannekeskitalo.unity.playersessionservice.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@RestController
@Api(tags = "Session Event entity", description = "Consumes session events and provides metrics")
@RequestMapping(path = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionEventController implements SessionEventResourceAPI {

    private final SessionService sessionService;

    public SessionEventController(@Autowired SessionService sessionService) {
        this.sessionService = sessionService;
    }

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
        log.info("Get {} entities", hours);


        List<SessionEventResponse> response = new ArrayList<SessionEventResponse>();
        IntStream.rangeClosed(1, hours).forEach(event -> {
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
    public ResponseEntity<SessionEventResponse> createSessionEvent(@RequestParam(value = "count", defaultValue = "1") int count,
            @Valid @RequestBody
            @ApiParam(name = "request",
                    value = "Create event request<br>Refer to model for detailed documentation",
                    required = true)
                    NewSessionEventRequest request) {

        Instant start = Instant.now();

        List<NewSessionEventRequest> requests = new ArrayList<NewSessionEventRequest>();
        IntStream.rangeClosed(1, count).forEach(i -> {
                    requests.add(NewSessionEventRequest.builder()
                            .country(request.getCountry())
                            .event(request.getCountry())
                            .playerId(request.getPlayerId())
                            .sessionId(UUID.randomUUID())
                            .ts(request.getTs())
                            .build());
                });

        sessionService.handleNewSessionEvent(requests);

        //log.info("Inserted {} events", requests.size());

        SessionEventResponse sessionEventResponse = SessionEventResponse.builder()
            .event(request.getEvent())
            .country(request.getCountry())
            .playerId(request.getPlayerId())
            .sessionId(request.getSessionId())
            .ts(request.getTs())
            .build();

        //log.info("Created event {}", sessionEventResponse);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        log.info("Elapsed time: {}, time per event: {}", timeElapsed, timeElapsed / count);

        return ResponseEntity.ok(sessionEventResponse);
    }
}
