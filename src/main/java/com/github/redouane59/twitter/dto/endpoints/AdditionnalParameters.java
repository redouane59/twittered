package com.github.redouane59.twitter.dto.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdditionnalParameters {

  @JsonProperty("start_time")
  private LocalDateTime startTime;
  @JsonProperty("end_time")
  private LocalDateTime endTime;
  @JsonProperty("since_id")
  private String        sinceId;
  @JsonProperty("until_id")
  private String        untilId;
  private String        granularity;
  @JsonProperty("next_token")
  private String        nextToken;
}
