package com.github.redouane59.twitter.dto.tweet;

import com.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// @todo mix with TweetSearchResponseV2 class ?
public class TweetListV2 {

  private List<TweetData> data;

}
