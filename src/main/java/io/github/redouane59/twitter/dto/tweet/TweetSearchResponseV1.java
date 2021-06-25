package io.github.redouane59.twitter.dto.tweet;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetSearchResponseV1 {

  private List<TweetV1> results;
  private String        next;
}
