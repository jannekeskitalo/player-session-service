package net.jannekeskitalo.unity.playersessionservice.api;

import java.time.LocalDateTime;

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
public class IngestEventResponse {
    @ApiModelProperty(value = "Event type", required = true, example = "10")
    @NotNull
    int ingestedEventCount;

    @ApiModelProperty(value = "Timestamp of the event", required = true, example = "2019-04-07T11:56:53.147")
    @NotNull
    LocalDateTime ts;

    @Tolerate
    public IngestEventResponse() {
    }
}

