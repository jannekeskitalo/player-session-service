package net.jannekeskitalo.unity.playersessionservice.querying;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
public class ResultConsumer<SessionStartedByCountry> implements Consumer<SessionStartedByCountry> {

    private final ResponseBodyEmitter emitter;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResultConsumer(ResponseBodyEmitter emitter) {
        this.emitter = emitter;
        log.info("ResultConsumer created");
    }

    @Override
    public void accept(SessionStartedByCountry session) {
        try {
            emitter.send(session, MediaType.APPLICATION_JSON_UTF8);
            emitter.send("\n", MediaType.TEXT_PLAIN);
            //log.info("Emitted: {}", session);
        } catch (IOException e) {
            log.error("Failed to emit an entity: {}", session, e);
        }
    }
}
