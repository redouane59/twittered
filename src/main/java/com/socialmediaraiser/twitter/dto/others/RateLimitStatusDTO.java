package com.socialmediaraiser.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class RateLimitStatusDTO {

    @JsonProperty("rate_limit_context")
    private RateLimitContextDTO rateLimitContext;
    private Map<String, JsonNode> resources;

    @Data
    @NoArgsConstructor
    public static class RateLimitContextDTO{
        @JsonProperty("access_token")
        private String accessToken;
    }
}
