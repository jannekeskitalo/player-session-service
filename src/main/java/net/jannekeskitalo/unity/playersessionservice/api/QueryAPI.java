package net.jannekeskitalo.unity.playersessionservice.api;

import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RequestMapping("/sessions")
public interface QueryAPI {

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}")
    ResponseEntity<QueryResponse> sessionsByPlayer(
            @ApiParam(required = true, name = "playerId", value = "Fetch sessions for this player")
            @PathVariable(name = "playerId") String playerId);

    @RequestMapping(method = RequestMethod.GET, path = "/by-country/{country}/{hour}")
    public ResponseEntity<ResponseBodyEmitter> sessionsByCountry(
            @ApiParam(required = true, name = "country", value = "Country as US/FI/SE", defaultValue = "FI")
            @PathVariable(name = "country") String country,
            @ApiParam(required = true, name = "hour", value = "Timestamp truncated to specific hour", defaultValue = "2019-04-13T17:00:00")
            @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
            @PathVariable(name = "hour") LocalDateTime hour) throws IOException;
}