package io.github.redouane59.twitter.dto.list;

import io.github.redouane59.twitter.dto.list.TwitterList.TwitterListData;
import io.github.redouane59.twitter.dto.tweet.TweetList.TweetMeta;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TwitterListList {

  private List<TwitterListData> data;
  private Includes              includes;
  private TweetMeta             meta;

}
