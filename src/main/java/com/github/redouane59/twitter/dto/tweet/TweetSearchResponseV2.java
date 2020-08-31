package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetSearchResponseV2 {

  private List<TweetV2.TweetData> data;
  private Meta                    meta;

  @Getter
  @Setter
  public static class Meta {

    @JsonProperty("newest_id")
    private String newestId;
    @JsonProperty("oldest_id")
    private String oldestId;
    @JsonProperty("next_token")
    private String nextToken;
    @JsonProperty("result_count")
    private int    resultCount;
  }
}
