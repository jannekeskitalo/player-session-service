package net.jannekeskitalo.unity.playersessionservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/sessions")
public interface QueryAPI {

    @RequestMapping(method = RequestMethod.GET, path = "/by-player/{playerId}")
    ResponseEntity sessionsByPlayer(@PathVariable(name = "id") UUID playerId);
}