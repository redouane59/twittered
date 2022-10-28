package io.github.redouane59.twitter.dto.dm;

import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import io.github.redouane59.twitter.dto.user.UserList.UserMeta;
import java.util.List;
import lombok.Getter;

@Getter
public class DirectMessage {

  List<DmEvent> data;

  Includes includes;
  UserMeta meta;
}
