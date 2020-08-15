package com.github.redouane59.twitter.dto.tweet;

import com.github.redouane59.twitter.dto.tweet.TweetDTOv2.TweetData;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TweetDTOListv2 {

  private List<TweetData> data;

}
