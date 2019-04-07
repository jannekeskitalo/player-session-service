package net.jannekeskitalo.unity.playersessionservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface SessionEventResourceAPI {

    @RequestMapping(method = RequestMethod.GET, path = "{country}")
    ResponseEntity<List<SessionEventResponse>> getEventsForCountry(
            @PathVariable("country") String country,
            @RequestParam(value = "hours", defaultValue = "1") int hours
    );

    @RequestMapping(method = RequestMethod.POST, path = "")
    ResponseEntity<SessionEventResponse> createSessionEvent(@RequestParam(value = "count", defaultValue = "1") int count, @Valid @RequestBody NewSessionEventRequest newSessionEventRequest);
}

