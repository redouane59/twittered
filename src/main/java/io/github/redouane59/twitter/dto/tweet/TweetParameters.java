package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
public class TweetParameters {

  private String        text;
  @JsonProperty("direct_message_deep_link")
  private String        directMessageDeepLink;
  @JsonProperty("for_super_followers_only")
  private boolean       forSuperFollowersOnly;
  private Geo           geo;
  // private Media   media;
  private Poll          poll;
  @JsonProperty("quote_tweet_id")
  private String        quoteTweetId;
  private Reply         reply;
  @JsonProperty("reply_settings")
  private ReplySettings replySettings;

  @Builder
  @Getter
  public static class Reply {

    @JsonProperty("exclude_reply_user_ids")
    private List<String> excludeReplyUserIds;
    @JsonProperty("in_reply_to_tweet_id")
    private List<String> inReplyToTweetId;

  }

  @Builder
  @Getter
  public static class Poll {

    @JsonProperty("poll.duration_minutes")
    private int          durationMinutes;
    private List<String> options;
  }


}
