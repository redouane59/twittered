package io.github.redouane59.twitter.dto.tweet;

import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TweetListV2 {

  List<TweetData> data;
}
