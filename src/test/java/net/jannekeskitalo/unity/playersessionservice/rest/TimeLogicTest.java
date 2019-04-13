package net.jannekeskitalo.unity.playersessionservice.rest;

import net.jannekeskitalo.unity.playersessionservice.querying.TimeLogic;
import org.junit.Before;
import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TimeLogicTest {

    Clock clock;

    @Before
    public void setup() {
        clock = Clock.fixed(Instant.parse("2019-04-12T10:15:30.00Z"), ZoneId.of("UTC"));
    }

    @Test
    public void test_last_correct_hours() {
        assertThat(TimeLogic.timestampsForLastHours(1, clock), equalTo(Collections.singletonList(LocalDateTime.parse("2019-04-12T10:00:00"))));
        assertThat(TimeLogic.timestampsForLastHours(3, clock),
                equalTo(Arrays.asList(
                        LocalDateTime.parse("2019-04-12T10:00:00"),
                        LocalDateTime.parse("2019-04-12T09:00:00"),
                        LocalDateTime.parse("2019-04-12T08:00:00"))));
    }
}
