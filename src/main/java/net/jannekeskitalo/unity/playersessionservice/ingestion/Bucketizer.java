package net.jannekeskitalo.unity.playersessionservice.ingestion;

import java.util.concurrent.ThreadLocalRandom;

public class Bucketizer {

    public static int BUCKET_COUNT = 10;

    // This should be changed to determi
    public static int bucketId() {
        return ThreadLocalRandom.current().nextInt(1, BUCKET_COUNT + 1);
    }
}
