package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.stream.StreamRules;
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

/**
 * @version labs
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TweetV2 implements Tweet {

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
    private JsonNode                 entities;
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

}
