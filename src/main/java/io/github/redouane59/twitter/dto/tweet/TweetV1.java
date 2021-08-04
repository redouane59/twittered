package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.tweet.entities.BaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.TextBaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
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
  private              EntitiesV1     entities;

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
  public String getSource() {
    LOGGER.error(NOT_IMPLEMENTED_EXECEPTION);
    return null;
  }

  @Override
  public Entities getEntities() {
    return entities;
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


  @Getter
  @Setter
  public static class EntitiesV1 implements Entities {
    @JsonProperty("hashtags")
    private List<HashtagEntityV1> hashtags;
    @JsonProperty("urls")
    private List<UrlEntityV1> urls;
    @JsonProperty("user_mentions")
    private List<UserMentionEntityV1> userMentions;
    @JsonProperty("symbols")
    private List<SymbolEntityV1> symbols;
    private JsonNode media; //TODO We should implement media objects accordingly
  }

  @Getter
  @Setter
  public static class BaseEntityV1 implements BaseEntity {
    @JsonProperty("indices")
    private int[] indices;

    @Override
    public int getStart() {
      return indices[0];
    }

    @Override
    public int getEnd() {
      return indices[1];
    }
  }

  @Getter
  @Setter
  public static class TextBaseEntityV1 extends BaseEntityV1 implements TextBaseEntity {
    @JsonProperty("text")
    private String text;

    @Override
    public String getText() {
      return text;
    }
  }

  @Getter
  @Setter
  public static class UrlEntityV1 extends BaseEntityV1 implements UrlEntity {
    @JsonProperty("url")
    private String url;
    @JsonProperty("display_url")
    private String displayUrl;
    @JsonProperty("expanded_url")
    private String expandedUrl;
    @JsonProperty("unwound")
    private UnwoundUrlEntity unwound;

    @Override
    public int getStatus() {
      if(unwound != null) {
        return unwound.getStatus();
      }
      return -1;
    }

    @Override
    public String getDescription() {
      if(unwound != null) {
        return unwound.getDescription();
      }
      return null;
    }

    @Override
    public String getTitle() {
      if(unwound != null) {
        return unwound.getTitle();
      }
      return null;
    }

    @Override
    public String getUnwoundedUrl() {
      if(unwound != null) {
        return unwound.getUrl();
      }
      return null;
    }
  }

  @Getter
  @Setter
  public static class UnwoundUrlEntity {
    @JsonProperty("url")
    private String url;
    @JsonProperty("status")
    private int status;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
  }

  @Getter
  @Setter
  public static class HashtagEntityV1 extends TextBaseEntityV1 implements HashtagEntity{
  }

  @Getter
  @Setter
  public static class UserMentionEntityV1 extends TextBaseEntityV1 implements UserMentionEntity{
    @JsonProperty("name")
    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("id")
    private long id;

    @Override
    public String getText() {
      return getName();
    }
  }

  @Getter
  @Setter
  public static class SymbolEntityV1 extends TextBaseEntityV1 implements SymbolEntity{
  }
}
