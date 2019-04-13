package net.jannekeskitalo.unity.playersessionservice.querying;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.exception.TimeLogicException;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class TimeLogic {

    private TimeLogic() {
    }

    public static List<LocalDateTime> timestampsForLastHours(int hoursToGoBack, Clock clock) {

        if (hoursToGoBack < 1) {
            throw new TimeLogicException("hoursToGoBack was less than 1");
        }

        LocalDateTime ts = LocalDateTime.now(clock);

        return IntStream.rangeClosed(1, hoursToGoBack)
                .mapToObj(n -> ts.minusHours(n - 1).truncatedTo(ChronoUnit.HOURS))
                .collect(Collectors.toList());
    }
}
