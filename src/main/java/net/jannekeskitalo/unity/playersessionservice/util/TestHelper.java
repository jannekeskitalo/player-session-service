package net.jannekeskitalo.unity.playersessionservice.util;

import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestHelper {

    long timerStartNanos = 0;

    public static IngestEventRequest createIngestEventRequestOfSize(int size) {

        List<IngestEvent> ingestEvents = new ArrayList<>();
        IntStream.rangeClosed(1, size).forEach(i -> {
            ingestEvents.add(IngestEvent.builder()
                    .country("FI")
                    .event("start")
                    .playerId(UUID.randomUUID().toString())
                    .sessionId(UUID.randomUUID())
                    .ts(LocalDateTime.now())
                    .build());
        });
        return IngestEventRequest.builder()
                .eventBatch(ingestEvents)
                .build();
    }

    public void startTimer() {
        timerStartNanos = System.nanoTime();
    }

    public long stopTimer() {
        return System.nanoTime() - timerStartNanos;
    }

}
