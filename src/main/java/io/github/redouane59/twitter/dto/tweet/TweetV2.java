package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.dto.tweet.entities.BaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.TextBaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV2;
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
 * @version labs
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class TweetV2 implements Tweet {

  private static final String  NOT_IMPLEMENTED_EXECEPTION = "not implemented";
  private TweetData                data;
  private Includes                 includes;
  @JsonProperty("matching_rules")
  private StreamRules.StreamRule[] matchingRules;

  @Override
  public String getInReplyToStatusId() {
    if (data == null || data.getReferencedTweets() == null || data.getReferencedTweets().isEmpty()) {
      return null;
    }
    return data.getReferencedTweets().get(0).getId();
  }

  @Override
  public String getInReplyToStatusId(TweetType type) {
    if (data == null || data.getReferencedTweets() == null || data.getReferencedTweets().isEmpty()) {
      return null;
    }
    for (ReferencedTweetDTO referencedTweetDTO : data.getReferencedTweets()) {
      if (referencedTweetDTO.getType() == type) {
        return referencedTweetDTO.getId();
      }
    }
    return null;
  }

  @Override
  public String getLang() {
    if (data == null) {
      return null;
    }
    return data.getLang();
  }

  @Override
  public String getId() {
    if (data == null) {
      return null;
    }
    return data.getId();
  }

  @Override
  public String getText() {
    if (data == null) {
      return null;
    }
    return data.getText();
  }

  @Override
  public String getConversationId() {
    if (data == null) {
      return null;
    }
    return data.getConversationId();
  }

  @Override
  public ReplySettings getReplySettings() {
    if (data == null) {
      return null;
    }
    return data.getReplySettings();
  }

  @Override
  public Geo getGeo() {
    if (data == null) {
      return null;
    }
    return data.getGeo();
  }

  @Override
  public Attachments getAttachments() {
    if (data == null) {
      return null;
    }
    return data.getAttachments();
  }

  @Override
  public String getSource() {
    if (data == null) {
      return null;
    }
    return data.getSource();
  }

  @Override
  public Entities getEntities() {
    if (data == null) {
      return null;
    }
    return data.getEntities();
  }

  @Override
  public int getRetweetCount() {
    if (data == null) {
      return 0;
    }
    return data.getPublicMetrics().getRetweetCount();
  }

  @Override
  public int getLikeCount() {
    if (data == null) {
      return 0;
    }
    return data.getPublicMetrics().getLikeCount();
  }

  @Override
  public int getReplyCount() {
    if (data == null) {
      return 0;
    }
    return data.getPublicMetrics().getReplyCount();
  }

  @Override
  public int getQuoteCount() {
    if (data == null) {
      return 0;
    }
    return data.getPublicMetrics().getQuoteCount();
  }

  @Override
  public String getInReplyToUserId() {
    if (data == null) {
      return null;
    }
    return data.getInReplyToUserId();
  }

  @Override
  public User getUser() {
    if (includes == null) {
      return null;
    }
    return includes.getUsers().get(0);
  }

  @Override
  public String getAuthorId() {
    if (data == null) {
      return null;
    }
    return data.getAuthorId();
  }

  @Override
  public LocalDateTime getCreatedAt() {
    if (data == null) {
      return null;
    }
    return data.getCreatedAt();
  }

  @Override
  public List<ContextAnnotation> getContextAnnotations() {
    if (data == null) {
      return Arrays.asList();
    }
    return data.getContextAnnotations();
  }

  @Override
  public TweetType getTweetType() {
    if (data == null || data.referencedTweets == null || data.referencedTweets.isEmpty()) {
      return TweetType.DEFAULT;
    } else if (data.getReferencedTweets().size() > 1
               && (data.getReferencedTweets().get(0).getType().equals(TweetType.RETWEETED)
                   || data.getReferencedTweets().get(1).getType().equals(TweetType.RETWEETED))) {
      return TweetType.RETWEETED;
    }
    return data.getReferencedTweets().get(0).getType();
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TweetData implements Tweet {

    private String                   id;
    @JsonProperty("created_at")
    private String                   createdAt;
    @JsonAlias({"text", "full_text"})
    private String                   text;
    @JsonProperty("author_id")
    private String                   authorId;
    @JsonProperty("in_reply_to_user_id")
    private String                   inReplyToUserId;
    @JsonProperty("referenced_tweets")
    private List<ReferencedTweetDTO> referencedTweets;
    private EntitiesV2 entities;
    @JsonProperty("public_metrics")
    @JsonInclude(Include.NON_NULL)
    private TweetPublicMetricsDTO    publicMetrics;
    @JsonProperty("possibly_sensitive")
    private boolean                  possiblySensitive;
    private String                   lang;
    @JsonProperty("context_annotations")
    private List<ContextAnnotation>  contextAnnotations;
    @JsonProperty("conversation_id")
    private String                   conversationId;
    @JsonProperty("reply_settings")
    private ReplySettings            replySettings;
    private Geo                      geo;
    private Attachments              attachments;
    private String                   source;

    @Override
    @JsonIgnore
    public int getRetweetCount() {
      return publicMetrics.getRetweetCount();
    }

    @Override
    @JsonIgnore
    public int getLikeCount() {
      return publicMetrics.getLikeCount();
    }

    @Override
    @JsonIgnore
    public int getReplyCount() {
      return publicMetrics.getReplyCount();
    }

    @Override
    @JsonIgnore
    public int getQuoteCount() {
      return publicMetrics.getQuoteCount();
    }

    @Override
    public String getInReplyToStatusId() {
      if (referencedTweets == null || referencedTweets.isEmpty()) {
        return null;
      }
      return referencedTweets.get(0).getId();
    }

    @Override
    public String getInReplyToStatusId(TweetType type) {
      if (referencedTweets == null || referencedTweets.isEmpty()) {
        return null;
      }
      for (ReferencedTweetDTO referencedTweetDTO : referencedTweets) {
        if (referencedTweetDTO.getType() == type) {
          return referencedTweetDTO.getId();
        }
      }
      return null;
    }

    @Override
    public TweetType getTweetType() {
      if (referencedTweets == null || referencedTweets.isEmpty()) {
        return TweetType.DEFAULT;
      } else if (getReferencedTweets().size() > 1
                 && (getReferencedTweets().get(0).getType().equals(TweetType.RETWEETED)
                     || getReferencedTweets().get(1).getType().equals(TweetType.RETWEETED))) {
        return TweetType.RETWEETED;
      }
      return getReferencedTweets().get(0).getType();
    }

    @Override
    @JsonIgnore
    public User getUser() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthorId() {
      return authorId;
    }

    @Override
    public LocalDateTime getCreatedAt() {
      return ConverterHelper.getDateFromTwitterStringV2(createdAt);
    }

  }

  @Getter
  @Setter
  public static class ReferencedTweetDTO {

    private TweetType type;
    private String    id;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Includes {

    private List<UserV2.UserData>   users;
    private List<TweetV2.TweetData> tweets;
    private JsonNode media; //TODO We should implement media objects accordingly
  }


  @Getter
  @Setter
  public static class TweetPublicMetricsDTO {

    @JsonProperty("retweet_count")
    private int retweetCount;
    @JsonProperty("reply_count")
    private int replyCount;
    @JsonProperty("like_count")
    private int likeCount;
    @JsonProperty("quote_count")
    private int quoteCount;
  }

  @Getter
  @Setter
  public static class EntitiesV2 implements Entities {

    @JsonProperty("hashtags")
    private List<HashtagEntityV2> hashtags;
    @JsonProperty("urls")
    private List<UrlEntityV2> urls;
    @JsonProperty("mentions")
    private List<UserMentionEntityV2> userMentions;
    @JsonProperty("cashtags")
    private List<CashtagEntityV2> symbols;
  }

  @Getter
  @Setter
  public static class BaseEntityV2 implements BaseEntity {
    @JsonProperty("start")
    private int start;
    @JsonProperty("end")
    private int end;
  }

  @Getter
  @Setter
  public static class TextBaseEntityV2 extends BaseEntityV2 implements TextBaseEntity {
    @JsonProperty("tag")
    private String tag;

    @Override
    public String getText() {
      return tag;
    }
  }

  @Getter
  @Setter
  public static class UrlEntityV2 extends BaseEntityV2 implements UrlEntity {
    @JsonProperty("url")
    private String url;
    @JsonProperty("display_url")
    private String displayUrl;
    @JsonProperty("expanded_url")
    private String expandedUrl;
    @JsonProperty("status")
    private int status;
    @JsonProperty("description")
    private String description;
    @JsonProperty("title")
    private String title;
    @JsonProperty("unwound_url")
    private String unwoundedUrl;
  }

  @Getter
  @Setter
  public static class HashtagEntityV2 extends TextBaseEntityV2 implements HashtagEntity{
  }

  @Getter
  @Setter
  public static class UserMentionEntityV2 extends TextBaseEntityV2 implements UserMentionEntity{
    @JsonProperty("username")
    private String username;

    @Override
    public String getText() {
      return getUsername();
    }

    @Override
    public String getTag() {
      return getUsername();
    }
  }

  @Getter
  @Setter
  public static class CashtagEntityV2 extends TextBaseEntityV2 implements SymbolEntity{
  }
}
