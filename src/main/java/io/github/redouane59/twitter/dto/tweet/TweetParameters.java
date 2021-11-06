package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TweetParameters {

  private String        text;
  @JsonProperty("direct_message_deep_link")
  /**
   * https://twitter.com/messages/compose?recipient_id=<user_id>
   */
  private String        directMessageDeepLink;
  @JsonProperty("for_super_followers_only")
  private boolean       forSuperFollowersOnly;
  private Geo           geo;
  private Media         media;
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
    private String       inReplyToTweetId;

  }

  @Builder
  @Getter
  public static class Poll {

    @JsonProperty("duration_minutes")
    private int          durationMinutes;
    private List<String> options;
  }

  @Builder
  @Getter
  public static class Media {

    @JsonProperty("media_ids")
    private List<String> mediaIds;
    @JsonProperty("tagged_user_ids")
    private List<String> taggerUserIds;

  }


}
