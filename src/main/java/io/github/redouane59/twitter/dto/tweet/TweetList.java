package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = TweetListDeserializer.class)
public class TweetList {

  private List<TweetV2.TweetData> data;
  private TweetMeta               meta;
  private Includes                includes;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TweetMeta {

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
