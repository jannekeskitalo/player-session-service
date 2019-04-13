package net.jannekeskitalo.unity.playersessionservice.util;

import lombok.Builder;
import net.jannekeskitalo.unity.playersessionservice.ingestion.IngestRepository;

public class TestHelper {

    public TestHelper() {
        this.startTimer();
    }

    private long timerStartNanos = 0;

    public static TestHelper startiming() {
        return new TestHelper();
    }

    public void startTimer() {
        timerStartNanos = System.nanoTime();
    }

    public long stopTimer() {
        return System.nanoTime() - timerStartNanos;
    }

}
