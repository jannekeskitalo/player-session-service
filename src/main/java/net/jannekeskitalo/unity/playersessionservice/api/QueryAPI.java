package net.jannekeskitalo.unity.playersessionservice.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import java.io.IOException;

@RequestMapping("/sessions")
@Api(tags = "Querying", description = "Query last sessions by player or country")
public interface QueryAPI {

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}")
    @ApiOperation(value = "Query last 20 complete sessions for a player")
    ResponseEntity<QueryResponse> sessionsByPlayer(
            @ApiParam(required = true, name = "playerId", value = "Fetch sessions for this player")
            @PathVariable(name = "playerId") String playerId);

    @RequestMapping(method = RequestMethod.GET, path = "/by-country/{country}/{hours}")
    @ApiOperation(value = "Query last started sessions for a country")
    public ResponseEntity<ResponseBodyEmitter> sessionsByCountry(
            @ApiParam(required = true, name = "country", value = "Country as US/FI/SE", defaultValue = "FI")
            @PathVariable(name = "country") String country,
            //@ApiParam(required = true, name = "hours", value = "Fetch sessions for the last x hours") - doesnt' work, a swagger bug?
            @PathVariable(name = "hours") int hours) throws IOException;
}