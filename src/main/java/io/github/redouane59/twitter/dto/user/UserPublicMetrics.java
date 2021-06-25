package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicMetrics {

  @JsonProperty("followers_count")
  private int followersCount;
  @JsonProperty("following_count")
  private int followingCount;
  @JsonProperty("tweet_count")
  private int tweetCount;
  @JsonProperty("listed_count")
  private int listedCount;
}
