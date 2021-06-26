package io.github.redouane59.twitter.dto.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AdditionalParameters {

  public static final String SINCE_ID         = "since_id";
  public static final String UNTIL_ID         = "until_id";
  public static final String START_TIME       = "start_time";
  public static final String END_TIME         = "end_time";
  public static final String MAX_RESULTS      = "max_results";
  public static final String NEXT_TOKEN       = "next_token";
  public static final String PAGINATION_TOKEN = "pagination_token";
  public static final String GRANULARITY      = "granularity";


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
  @JsonProperty("pagination_token")
  private String        paginationToken;
  @JsonProperty("max_results")
  private int           maxResults;

  public Map<String, String> getMapFromParameters() {
    Map<String, String> parameters = new HashMap<>();
    if (this.getGranularity() != null) {
      parameters.put(GRANULARITY, this.getGranularity());
    }
    if (this.getStartTime() != null) {
      parameters.put(START_TIME, ConverterHelper.getStringFromDateV2(this.getStartTime()));
    }
    if (this.getEndTime() != null) {
      parameters.put(END_TIME, ConverterHelper.getStringFromDateV2(this.getEndTime()));
    }
    if (this.getSinceId() != null) {
      parameters.put(SINCE_ID, this.getSinceId());
    }
    if (this.getUntilId() != null) {
      parameters.put(UNTIL_ID, this.getUntilId());
    }
    if (this.getNextToken() != null) {
      parameters.put(NEXT_TOKEN, this.getNextToken());
    }
    if (this.getPaginationToken() != null) {
      parameters.put(PAGINATION_TOKEN, this.getPaginationToken());
    }
    if (this.getMaxResults() > 0) {
      parameters.put(MAX_RESULTS, String.valueOf(this.getMaxResults()));
    }
    return parameters;
  }

}
