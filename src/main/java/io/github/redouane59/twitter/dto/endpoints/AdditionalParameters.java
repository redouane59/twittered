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

  public static final String  SINCE_ID          = "since_id";
  public static final String  UNTIL_ID          = "until_id";
  public static final String  START_TIME        = "start_time";
  public static final String  END_TIME          = "end_time";
  public static final String  MAX_RESULTS       = "max_results";
  public static final String  NEXT_TOKEN        = "next_token";
  public static final String  PAGINATION_TOKEN  = "pagination_token";
  public static final String  GRANULARITY_COUNT = "granularity";
  @Builder.Default
  /**
   * If set to true, will loop the call until next_token is null to provide a full answer.
   * If set to false, will make just one call and return the next_token if it exists
   */
  private             boolean recursiveCall     = true;

  @JsonProperty("start_time")
  /**
   * The oldest UTC timestamp (from most recent seven days) from which the Tweet counts will be provided. Timestamp is in second granularity and is
   * inclusive (for example, 12:00:01 includes the first second of the minute). If included with the same request as a since_id parameter, only
   * since_id will be used. By default, a request will return Tweet counts from up to seven days ago if you do not include this parameter.
   */
  private LocalDateTime startTime;
  @JsonProperty("end_time")
  /**
   * The newest, most recent UTC timestamp to which the Tweet counts will be provided. Timestamp is in second granularity and is exclusive
   * (for example, 12:00:01 excludes the first second of the minute). By default, a request will return Tweet counts from as recent as 30 seconds ago
   * if you do not include this parameter.
   */
  private LocalDateTime endTime;
  @JsonProperty("since_id")
  /**
   * Returns results with a Tweet ID greater than (that is, more recent than) the specified ID. The ID specified is exclusive and responses will not
   * include it. If included with the same request as a start_time parameter, only since_id will be used.
   */
  private String        sinceId;
  @JsonProperty("until_id")
  /**
   * 	Returns results with a Tweet ID less than (that is, older than) the specified ID. The ID specified is exclusive and responses will not include it.
   */
  private String        untilId;
  /**
   * This is the granularity that you want the timeseries count data to be grouped by. The default granularity, if not specified is hour. Currently
   * used for tweetCounts endpoints
   */
  private String        granularity;
  @JsonProperty("next_token")
  /**
   * This parameter is used to get the next 'page' of results. The value used with the parameter is pulled directly from the response provided by the
   * API, and should not be modified.
   */
  private String        nextToken;
  @JsonProperty("pagination_token")
  /**
   * Used to request the next page of results if all results weren't returned with the latest request, or to go back to the previous page of results.
   * To return the next page, pass the next_token returned in your previous response. To go back one page, pass the previous_token returned in your previous response.
   */
  private String        paginationToken;
  @JsonProperty("max_results")
  /**
   * The maximum number of results to be returned per call. Warning, if recursiveCall is not set to false, calling an endpoint will
   * return x times maxResults
   */
  private int           maxResults;

  public Map<String, String> getMapFromParameters() {
    Map<String, String> parameters = new HashMap<>();
    if (getGranularity() != null) {
      parameters.put(GRANULARITY_COUNT, getGranularity());
    }
    if (getStartTime() != null) {
      parameters.put(START_TIME, ConverterHelper.getStringFromDateV2(getStartTime()));
    }
    if (getEndTime() != null) {
      parameters.put(END_TIME, ConverterHelper.getStringFromDateV2(getEndTime()));
    }
    if (getSinceId() != null) {
      parameters.put(SINCE_ID, getSinceId());
    }
    if (getUntilId() != null) {
      parameters.put(UNTIL_ID, getUntilId());
    }
    if (getNextToken() != null) {
      parameters.put(NEXT_TOKEN, getNextToken());
    }
    if (getPaginationToken() != null) {
      parameters.put(PAGINATION_TOKEN, getPaginationToken());
    }
    if (getMaxResults() > 0) {
      parameters.put(MAX_RESULTS, String.valueOf(getMaxResults()));
    }
    return parameters;
  }

}
