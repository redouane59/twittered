package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.TweetV2.Place;
import io.github.redouane59.twitter.dto.tweet.entities.BaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.MediaEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.TextBaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
import io.github.redouane59.twitter.dto.user.UserV1;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

  private static final String     NOT_IMPLEMENTED_EXCEPTION = "not implemented";
  private              String     id;
  private              String     lang;
  @JsonProperty("retweet_count")
  private              int        retweetCount;
  @JsonProperty("favorite_count")
  private              int        likeCount;
  @JsonProperty("reply_count")
  private              int        replyCount;
  @JsonProperty("quote_count")
  private              int        quoteCount;
  @JsonAlias({"text", "full_text"})
  private              String     text;
  @JsonProperty("created_at")
  private              String     createdAt;
  private              UserV1     user;
  @JsonProperty("in_reply_to_status_id_str")
  private              String     inReplyToStatusId;
  @JsonProperty("in_reply_to_user_id_str")
  private              String     inReplyToUserId;
  @JsonProperty("is_quote_status")
  private              boolean    isQuoteStatus;
  private              EntitiesV1 entities;

  @Override
  public LocalDateTime getCreatedAt() {
    return ConverterHelper.getDateFromTwitterString(createdAt);
  }

  @Override
  public List<ContextAnnotation> getContextAnnotations() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
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
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return null;
  }

  @Override
  public ReplySettings getReplySettings() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return null;
  }

  @Override
  public Geo getGeo() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return new Geo();
  }

  @Override
  public Attachments getAttachments() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return new Attachments();
  }

  @Override
  public String getSource() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return null;
  }

  @Override
  public List<MediaEntityV1> getMedia() {
    if (entities != null) {
      return entities.getMedia();
    }
    return Collections.emptyList();
  }

  @Override
  public List<Place> getPlaces() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return Collections.emptyList();
  }

  @Override
  public List<StreamRule> getMatchingRules() {
    LOGGER.error(NOT_IMPLEMENTED_EXCEPTION);
    return Collections.emptyList();
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

    private List<HashtagEntityV1>     hashtags;
    private List<UrlEntityV1>         urls;
    @JsonProperty("user_mentions")
    private List<UserMentionEntityV1> userMentions;
    private List<SymbolEntityV1>      symbols;
    private List<MediaEntityV1>       media;
  }

  @Getter
  @Setter
  public static class BaseEntityV1 implements BaseEntity {

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

    private String text;

    @Override
    public String getText() {
      return text;
    }
  }

  @Getter
  @Setter
  public static class UrlEntityV1 extends BaseEntityV1 implements UrlEntity {

    private String           url;
    @JsonProperty("display_url")
    private String           displayUrl;
    @JsonProperty("expanded_url")
    private String           expandedUrl;
    private UnwoundUrlEntity unwound;

    @Override
    public int getStatus() {
      if (unwound != null) {
        return unwound.getStatus();
      }
      return -1;
    }

    @Override
    public String getDescription() {
      if (unwound != null) {
        return unwound.getDescription();
      }
      return null;
    }

    @Override
    public String getTitle() {
      if (unwound != null) {
        return unwound.getTitle();
      }
      return null;
    }

    @Override
    public String getUnwoundedUrl() {
      if (unwound != null) {
        return unwound.getUrl();
      }
      return null;
    }
  }

  @Getter
  @Setter
  public static class UnwoundUrlEntity {

    private String url;
    private int    status;
    private String title;
    private String description;
  }

  @Getter
  @Setter
  public static class HashtagEntityV1 extends TextBaseEntityV1 implements HashtagEntity {

  }

  @Getter
  @Setter
  public static class UserMentionEntityV1 extends TextBaseEntityV1 implements UserMentionEntity {

    private String name;
    @JsonProperty("screen_name")
    private String screenName;
    private long   id;

    @Override
    public String getText() {
      return getName();
    }
  }

  @Getter
  @Setter
  public static class SymbolEntityV1 extends TextBaseEntityV1 implements SymbolEntity {

  }

  @Getter
  @Setter
  public static class MediaEntityV1 extends BaseEntityV1 implements MediaEntity {

    private long                   id;
    private String                 url;
    @JsonProperty("display_url")
    private String                 displayUrl;
    @JsonProperty("expanded_url")
    private String                 expandedUrl;
    @JsonProperty("media_url_https")
    private String                 mediaUrl;
    private String                 type;
    private Map<String, MediaSize> sizes;
  }

  @Getter
  @Setter
  public static class MediaSize {

    @JsonProperty("w")
    private int    width;
    @JsonProperty("h")
    private int    height;
    private String resize;
  }
}
