package io.github.redouane59.twitter.dto.others;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RateLimitStatus {

  @JsonProperty("rate_limit_context")
  private RateLimitContext      rateLimitContext;
  private Map<String, JsonNode> resources;

  @Getter
  @Setter
  @NoArgsConstructor
  public static class RateLimitContext {

    @JsonProperty("access_token")
    private String accessToken;
  }
}
