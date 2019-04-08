package net.jannekeskitalo.unity.playersessionservice.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.sql.Timestamp;
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
public class IngestEventRequest {

    @ApiModelProperty(value = "Event list", required = true)
    @NotNull
    List<IngestEvent> eventBatch;

    @Tolerate
    public IngestEventRequest() {
    }


}

