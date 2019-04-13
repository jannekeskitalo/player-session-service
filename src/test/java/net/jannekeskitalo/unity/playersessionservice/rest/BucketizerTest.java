package net.jannekeskitalo.unity.playersessionservice.rest;

import net.jannekeskitalo.unity.playersessionservice.ingestion.Bucketizer;
import org.junit.Before;
import org.junit.Test;
import static net.jannekeskitalo.unity.playersessionservice.ingestion.Bucketizer.BUCKET_COUNT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BucketizerTest {

    @Test
    public void test_bucket_id_in_configured_range() {
        int bucketCount = BUCKET_COUNT;

        int bucketIdMin = 1;
        int bucketIdMax = bucketCount;
        int newBucketId;
        int count = 0;
        while(count <= 1000) {
            newBucketId = Bucketizer.bucketId();
            bucketIdMin = newBucketId < bucketIdMin ? newBucketId : bucketIdMin;
            bucketIdMax = newBucketId > bucketIdMax ? newBucketId : bucketIdMax;
            count++;
        }
        assertThat(bucketIdMax, lessThanOrEqualTo(bucketCount));
        assertThat(bucketIdMin, greaterThanOrEqualTo(1));
    }
}
