package io.github.redouane59.twitter.dto.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Includes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TwitterList {

  private TwitterListData data;
  private Includes        includes;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  public static class TwitterListData {

    String id;
    String name;
    String description;
    @JsonProperty("follower_count")
    int     followerCount;
    @JsonProperty("private")
    boolean isPrivate;
    @JsonProperty("owner_id")
    String  ownerId;
  }
}
