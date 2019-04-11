package net.jannekeskitalo.unity.playersessionservice.ingestion;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Quick & Dirty file loader for testing
 */
@Slf4j
@Service
public class IngestFileService {

    IngestService ingestService;

    @Autowired
    public IngestFileService(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    public void handleFile(MultipartFile file, int count) {

        ObjectMapper mapper = new ObjectMapper();

        int batchCount = 0;
        int eventCount = 0;
        int eventsInBatchCount = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            IngestEvent ingestEvent;
            List<IngestEvent> batch = new ArrayList<>();
            while (reader.ready()) {
                if (eventCount == count) { break; }
                String line = reader.readLine();
                ingestEvent = mapper.readValue(line, IngestEvent.class);
                eventCount++;
                if (eventsInBatchCount == 1) {
                    batch = new ArrayList<>();
                    batch.add(ingestEvent);
                    eventsInBatchCount++;
                } else if (eventsInBatchCount < 10) {
                    batch.add(ingestEvent);
                    eventsInBatchCount++;
                } else {
                    batch.add(ingestEvent);
                    ingestService.handleEventBatchAsync(IngestEventRequest.builder().eventBatch(batch).build());
                    batch = null;
                    eventsInBatchCount = 1;
                    batchCount++;
                    if (batchCount % 1000 == 0) {
                        log.info("Batches: {}, events: {}", batchCount, eventCount);
                    }
                }
            }
            if (batch != null) {
                ingestService.handleEventBatchAsync(IngestEventRequest.builder().eventBatch(batch).build());
            };

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Total batches: {}, events: {}", batchCount, eventCount);
    }
}
