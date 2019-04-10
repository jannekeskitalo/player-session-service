package net.jannekeskitalo.unity.playersessionservice.api;

import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/sessions")
public interface QueryAPI {

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}")
    ResponseEntity<QueryResponse> sessionsByPlayer(
            @ApiParam(required = true, name = "playerId", value = "Fetch sessions for this player")
            @PathVariable(name = "playerId") String playerId);
}