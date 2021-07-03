package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.user.UserV1;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.Arrays;
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

  private static final String  NOT_IMPLEMENTED_EXECEPTION = "not implemented";
  private              String  id;
  private              String  lang;
  @JsonProperty("retweet_count")
  private              int     retweetCount;
  @JsonProperty("favorite_count")
  private              int     likeCount;
  @JsonProperty("reply_count")
  private              int     replyCount;
  @JsonProperty("quote_count")
  private              int     quoteCount;
  @JsonAlias({"text", "full_text"})
  private              String  text;
  @JsonProperty("created_at")
  private              String  createdAt;
  private              UserV1  user;
  @JsonProperty("in_reply_to_status_id_str")
  private              String  inReplyToStatusId;
  @JsonProperty("in_reply_to_user_id_str")
  private              String  inReplyToUserId;
  @JsonProperty("is_quote_status")
  private              boolean isQuoteStatus;

  @Override
  public LocalDateTime getCreatedAt() {
    return ConverterHelper.getDateFromTwitterString(createdAt);
  }

  @Override
  public List<ContextAnnotation> getContextAnnotations() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return Arrays.asList();
  }

  @Override
  public TweetType getTweetType() {
    if (isQuoteStatus) {
      return TweetType.QUOTED;
    }
    return TweetType.DEFAULT;
  }

  @Override
  public String getConversationId() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return null;
  }

  @Override
  public ReplySettings getReplySettings() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return null;
  }

  @Override
  public Geo getGeo() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return new Geo();
  }

  @Override
  public Attachments getAttachments() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return new Attachments();
  }

  @Override
  public String getInReplyToStatusId(TweetType type) {
    return getInReplyToStatusId();
  }

  @Override
  public String getAuthorId() {
    if (user == null) {
      return null;
    }
    return user.getId();
  }
}
