package net.jannekeskitalo.unity.playersessionservice.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestEvent {

    @ApiModelProperty(value = "Event type", required = true, example = "start")
    @NotNull
    String event;

    @ApiModelProperty(value = "Player ID", required = true, example = "2d4073e4-6ceb-4d0a-9d40-be301b9437ed")
    String playerId;

    @ApiModelProperty(value = "Country code", required = true, example = "FI")
    @NotNull
    String country;

    @ApiModelProperty(value = "Session ID", required = true, example = "2d4073e4-6ceb-4d0a-9d40-be301b9437ed")
    @NotNull
    UUID sessionId;

    @ApiModelProperty(value = "Timestamp of the event", required = true, example = "2019-04-07T11:56:53.147")
    @NotNull
    LocalDateTime ts;

    @Tolerate
    public IngestEvent() {
    }

    public void setTs(String ts) {
        this.ts = LocalDateTime.parse(ts, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
