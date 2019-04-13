package net.jannekeskitalo.unity.playersessionservice.util;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import net.jannekeskitalo.unity.playersessionservice.ingestion.IngestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.jannekeskitalo.unity.playersessionservice.ingestion.Bucketizer.bucketId;

@Slf4j
@Service
public class TestDataHelper {

    @Autowired
    IngestRepository ingestRepository;

    public void generateTestData(int count) {

        for (int i = 0; i < count; i++) {
            SessionStartedByCountry s = SessionStartedByCountry.builder()
                    .startTs(LocalDateTime.now())
                    .startHourTs(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                    .sessionId(UUID.randomUUID())
                    .bucket(bucketId())
                    .playerId("c201d8166ae24796bf1c3ffdabdd8ec8")
                    .country("FI")
                    .build();
            //log.info("Test item: {}", s);
            CompletableFuture f = ingestRepository.insertSessionStartedByCountry(s);
            try {
                f.get();
            } catch (Exception e) {
                log.error("Failed to insert item: {}", s);
            }
        }
    }

}
