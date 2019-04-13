package net.jannekeskitalo.unity.playersessionservice.ingestion;

import java.util.concurrent.ThreadLocalRandom;
/**
 * A simple fixed size bucketing solution.
 *
 * This could be made dynamic and adapt to ingestion load. For sure the bucket count
 * needs to be larger for production use. More about that in README.
 *
 * The bucket count is now also directly bound to the query implementation. The dynamic
 * version would store the bucket metadata in cassandra table and the query logic would
 * check it from there.
 */
public class Bucketizer {

    public static int BUCKET_COUNT = 100;

    // This should be changed to deterministic value, for example derive it from the player_id
    public static int bucketId() {
        return ThreadLocalRandom.current().nextInt(1, BUCKET_COUNT + 1);
    }
}
