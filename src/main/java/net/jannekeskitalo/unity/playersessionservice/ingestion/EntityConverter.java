package net.jannekeskitalo.unity.playersessionservice.ingestion;

import com.datastax.driver.core.Session;
import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEventRequest;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionCompleteByPlayer;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Validated
public class EntityConverter {

    public Optional<SessionById> toSessionById(IngestEvent event) {

        return Optional.of(SessionById.builder()
            .sessionId(event.getSessionId())
            .playerId(event.getPlayerId())
            .startTs(getPossibleStartTsOrNull(event))
            .endTs(getPossibleEndTsOrNull(event))
            .startHourTs(event.getTs().truncatedTo(ChronoUnit.HOURS))
            .endHourTs(event.getTs().truncatedTo(ChronoUnit.HOURS))
            .country(event.getCountry())
            .build());
    }

    public Optional<SessionCompleteByPlayer> toSessionCompleteByPlayer(IngestEvent event) {
        return Optional.of(SessionCompleteByPlayer.builder()
                .playerId(event.getPlayerId())
                .sessionId(event.getSessionId())
                .country(event.getCountry())
                .startTs(getPossibleStartTsOrNull(event))
                .endTs(getPossibleEndTsOrDefault(event))
                .build());
    }

    public Optional<SessionStartedByCountry> toSessionStartedByCountry(IngestEvent event) {
        if (event.getCountry() != null) {
            return Optional.of(SessionStartedByCountry.builder()
                    .country(event.getCountry())
                    .startHourTs(getPossibleStartHourTsOrNull(event))
                    .bucket(1)
                    .sessionId(event.getSessionId())
                    .playerId(event.getPlayerId())
                    .startTs(getPossibleStartTsOrNull(event))
                    .endTs(getPossibleEndTsOrNull(event))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private LocalDateTime getPossibleStartHourTsOrNull(@NotNull IngestEvent event) {
        return getPossibleStartTsOrNull(event).truncatedTo(ChronoUnit.HOURS);
    }

    private LocalDateTime getPossibleEndHourTsOrNull(@NotNull IngestEvent event) {
        return getPossibleEndTsOrNull(event).truncatedTo(ChronoUnit.HOURS);
    }


    private LocalDateTime getPossibleStartTsOrNull(@NotNull IngestEvent event) {
        return event.getEvent().equals("start") ? event.getTs() : null;
    }

    private LocalDateTime getPossibleEndTsOrNull(@NotNull IngestEvent event) {
        return event.getEvent().equals("end") ? event.getTs() : null;
    }

    private LocalDateTime getPossibleEndTsOrDefault(@NotNull IngestEvent event) {
        return event.getEvent().equals("end") ? event.getTs() : LocalDateTime.parse("1970-01-01T00:00:00");
    }

}
