package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.dto.user.UserV1;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @version current
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Slf4j
public class TweetV1 implements Tweet {

  private String  id;
  private String  lang;
  @JsonProperty("retweet_count")
  private int     retweetCount;
  @JsonProperty("favorite_count")
  private int     likeCount;
  @JsonProperty("reply_count")
  private int     replyCount;
  @JsonProperty("quote_count")
  private int     quoteCount;
  @JsonAlias({"text", "full_text"})
  private String  text;
  @JsonProperty("created_at")
  private String  createdAt;
  private UserV1  user;
  @JsonProperty("in_reply_to_status_id_str")
  private String  inReplyToStatusId;
  @JsonProperty("in_reply_to_user_id_str")
  private String  inReplyToUserId;
  @JsonProperty("is_quote_status")
  private boolean isQuoteStatus;

  public LocalDateTime getCreatedAt() {
    return ConverterHelper.getDateFromTwitterString(this.createdAt);
  }

  @Override
  public List<ContextAnnotation> getContextAnnotations() {
    LOGGER.error("not implemented");
    return new ArrayList<>();
  }

  @Override
  public TweetType getTweetType() {
    if (this.isQuoteStatus) {
      return TweetType.QUOTED;
    }
    return TweetType.DEFAULT;
  }

  @Override
  public String getConversationId() {
    LOGGER.error("not implemented");
    return null;
  }

  @Override
  public ReplySettings getReplySettings() {
    LOGGER.error("not implemented");
    return null;
  }

  @Override
  public String getInReplyToStatusId(TweetType type) {
    return this.getInReplyToStatusId();
  }

  @Override
  public String getAuthorId() {
    if (this.user == null) {
      return null;
    }
    return this.user.getId();
  }
}
