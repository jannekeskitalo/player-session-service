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

    public void handleFile(MultipartFile file) {

        ObjectMapper mapper = new ObjectMapper();

        int batchCount = 0;
        int eventCount = 0;
        int count = 1;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            IngestEvent ingestEvent;
            List<IngestEvent> ingestEventList = new ArrayList<>();
            while (reader.ready()) {
                String line = reader.readLine();
                ingestEvent = mapper.readValue(line, IngestEvent.class);
                eventCount++;
                if (count == 1) {
                    ingestEventList = new ArrayList<>();
                    ingestEventList.add(ingestEvent);
                    count++;
                } else if (count < 10) {
                    ingestEventList.add(ingestEvent);
                    count++;
                } else {
                    ingestEventList.add(ingestEvent);
                    ingestService.handleEventBatchAsync(IngestEventRequest.builder().eventBatch(ingestEventList).build());
                    ingestEventList = null;
                    count = 1;
                    batchCount++;
                    if (batchCount % 1000 == 0) {
                        log.info("Batches: {}, events: {}", batchCount, eventCount);
                    }
                }
            }
            if (ingestEventList != null) {
                ingestService.handleEventBatchAsync(IngestEventRequest.builder().eventBatch(ingestEventList).build());
            };

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Total batches: {}, events: {}", batchCount, eventCount);
    }
}
