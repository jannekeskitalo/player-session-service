package net.jannekeskitalo.unity.playersessionservice.querying;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

@Slf4j
public class ResultConsumerStreamer<SessionStartedByCountry> implements Consumer<SessionStartedByCountry> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final OutputStream out;

    public ResultConsumerStreamer(OutputStream out) {
        this.out = out;
        log.info("ResultConsumer created");
    }

    @Override
    public void accept(SessionStartedByCountry session) {
        try {
            out.write(mapper.writeValueAsString(session).getBytes());
            //log.info("Emitted: {}", session);
        } catch (IOException e) {
            log.error("Failed to write to outputstream, entity: {}", session, e);
        }
    }
}