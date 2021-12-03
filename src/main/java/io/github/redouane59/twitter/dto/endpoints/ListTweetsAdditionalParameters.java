package io.github.redouane59.twitter.dto.endpoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@Builder
/**
 * Data class containing parameters for making a request that gets Tweets in a Twitter List for V1 API
 * https://developer.twitter.com/en/docs/twitter-api/v1/accounts-and-users/create-manage-lists/api-reference/get-lists-statuses
 */
public class ListTweetsAdditionalParameters {

    public static final String  SINCE_ID          = "since_id";
    public static final String  MAX_ID            = "max_id";
    public static final String  COUNT             = "count";

    @JsonProperty(SINCE_ID)
    /**
     * Returns results with a Tweet ID greater than (that is, more recent than) the specified ID. The ID specified is exclusive and responses will not
     * include it. If included with the same request as a start_time parameter, only since_id will be used.
     */
    private String        sinceId;
    @JsonProperty(MAX_ID)
    /**
     * Returns results with an ID less than (that is, older than) or equal to the specified ID.
     */
    private String        maxId;
    @JsonProperty(COUNT)
    /**
     * The maximum number of results to be returned per call. Warning, if recursiveCall is not set to false, calling an endpoint will
     * return x times maxResults
     */
    private int           count;

    public Map<String, String> getMapFromParameters() {
        Map<String, String> parameters = new HashMap<>();
        if (getCount() > 0) {
            parameters.put(COUNT, String.valueOf(getCount()));
        }
        if (getMaxId() != null) {
            parameters.put(MAX_ID, getMaxId());
        }
        if (getSinceId() != null) {
            parameters.put(SINCE_ID, getSinceId());
        }

        return parameters;
    }
}
