package net.jannekeskitalo.unity.playersessionservice.ingestion;

import lombok.extern.slf4j.Slf4j;
import net.jannekeskitalo.unity.playersessionservice.api.IngestEvent;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionById;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionByPlayerId;
import net.jannekeskitalo.unity.playersessionservice.domain.entity.SessionStartedByCountry;
import net.jannekeskitalo.unity.playersessionservice.exception.ConversionExceptionMissingKeyField;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import static net.jannekeskitalo.unity.playersessionservice.ingestion.Bucketizer.bucketId;

@Slf4j
@Component
@Validated
public class EntityConverter {

    public Optional<SessionById> toSessionById(IngestEvent event) {

        return Optional.of(SessionById.builder()
            .sessionId(event.getSessionId())
            .playerId(event.getPlayerId())
            .startTs(getPossibleStartTsOrNull(event).orElse(null))
            .endTs(getPossibleEndTsOrNull(event).orElse(null))
            .startHourTs(getPossibleStartHourTsOrNull(event).orElse(null))
            .endHourTs(getPossibleEndHourTsOrNull(event).orElse(null))
            .country(event.getCountry())
            .build());
    }

    public Optional<SessionByPlayerId> toSessionByPlayer(IngestEvent event) {
        return Optional.of(SessionByPlayerId.builder()
                .playerId(event.getPlayerId())
                .sessionId(event.getSessionId())
                .event(event.getEvent())
                .country(event.getCountry())
                .ts(event.getTs())
                .build());
    }

    public Optional<SessionStartedByCountry> toSessionStartedByCountry(IngestEvent event) {
        if (event.getEvent().equals("start")) {
            return Optional.of(SessionStartedByCountry.builder()
                    .country(event.getCountry())
                    .startHourTs(getPossibleStartHourTsOrNull(event).orElseThrow(() -> new ConversionExceptionMissingKeyField("startHourTs")))
                    .bucket(bucketId())
                    .sessionId(event.getSessionId())
                    .playerId(event.getPlayerId())
                    .startTs(getPossibleStartTsOrNull(event).orElse(null))
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<LocalDateTime> getPossibleStartHourTsOrNull(@NotNull IngestEvent event) {
        Optional<LocalDateTime> ts = getPossibleStartTsOrNull(event);
        if (ts.isPresent()) {
            return Optional.of(ts.get().truncatedTo(ChronoUnit.HOURS));
        } else {
            return Optional.empty();
        }
    }

    private Optional<LocalDateTime> getPossibleEndHourTsOrNull(@NotNull IngestEvent event) {
        Optional<LocalDateTime> ts = getPossibleEndTsOrNull(event);
        if (ts.isPresent()) {
            return Optional.of(ts.get().truncatedTo(ChronoUnit.HOURS));
        } else {
            return Optional.empty();
        }
    }

    private Optional<LocalDateTime> getPossibleStartTsOrNull(@NotNull IngestEvent event) {
        return Optional.ofNullable(event.getEvent().equals("start") ? event.getTs() : null);
    }

    private Optional<LocalDateTime> getPossibleEndTsOrNull(@NotNull IngestEvent event) {
        return Optional.ofNullable(event.getEvent().equals("end") ? event.getTs() : null);
    }

    private LocalDateTime getPossibleEndTsOrDefault(@NotNull IngestEvent event) {
        return event.getEvent().equals("end") ? event.getTs() : LocalDateTime.parse("1970-01-01T00:00:00");
    }

}
