package io.github.redouane59.twitter.dto.tweet;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class TweetSearchResponse {

  private List<Tweet> tweets;
  private String      nextToken;

}
