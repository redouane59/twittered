package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV2;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

  private TweetData data;
  private Includes  includes;

  @Getter
  @Setter
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

    @Override
    public int getRetweetCount() {
      return this.publicMetrics.getRetweetCount();
    }

    @Override
    public int getLikeCount() {
      return this.publicMetrics.getLikeCount();
    }

    @Override
    public int getReplyCount() {
      return this.publicMetrics.getReplyCount();
    }

    @Override
    public int getQuoteCount() {
      return this.publicMetrics.getQuoteCount();
    }

    @Override
    public String getInReplyToStatusId() {
      if (this.referencedTweets == null || this.referencedTweets.isEmpty()) {
        return null;
      }
      return this.referencedTweets.get(0).getId();
    }

    @Override
    public String getInReplyToStatusId(TweetType type) {
      if (this.referencedTweets == null || this.referencedTweets.isEmpty()) {
        return null;
      }
      for (ReferencedTweetDTO referencedTweetDTO : this.referencedTweets) {
        if (referencedTweetDTO.getType() == type) {
          return referencedTweetDTO.getId();
        }
      }
      return null;
    }

    @Override
    public TweetType getTweetType() {
      if (this.referencedTweets == null || this.referencedTweets.isEmpty()) {
        return TweetType.DEFAULT;
      } else if (this.getReferencedTweets().size() > 1
                 && (this.getReferencedTweets().get(0).getType().equals(TweetType.RETWEETED)
                     || this.getReferencedTweets().get(1).getType().equals(TweetType.RETWEETED))) {
        return TweetType.RETWEETED;
      }
      return this.getReferencedTweets().get(0).getType();
    }

    @Override
    public User getUser() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthorId() {
      return this.authorId;
    }

    @Override
    public LocalDateTime getCreatedAt() {
      return ConverterHelper.getDateFromTwitterDateV2(this.createdAt);
    }

  }

  @Override
  public String getInReplyToStatusId() {
    if (this.data == null || this.data.getReferencedTweets() == null || this.data.getReferencedTweets().isEmpty()) {
      return null;
    }
    return this.data.getReferencedTweets().get(0).getId();
  }

  @Override
  public String getInReplyToStatusId(TweetType type) {
    if (this.data == null || this.data.getReferencedTweets() == null || this.data.getReferencedTweets().isEmpty()) {
      return null;
    }
    for (ReferencedTweetDTO referencedTweetDTO : this.data.getReferencedTweets()) {
      if (referencedTweetDTO.getType() == type) {
        return referencedTweetDTO.getId();
      }
    }
    return null;
  }

  @Override
  public String getLang() {
    if (this.data == null) {
      return null;
    }
    return this.data.getLang();
  }

  @Override
  public String getId() {
    if (this.data == null) {
      return null;
    }
    return this.data.getId();
  }

  @Override
  public String getText() {
    if (this.data == null) {
      return null;
    }
    return this.data.getText();
  }

  @Override
  public String getConversationId() {
    if (this.data == null) {
      return null;
    }
    return this.data.getConversationId();
  }

  @Override
  public ReplySettings getReplySettings() {
    if (this.data == null) {
      return null;
    }
    return this.data.getReplySettings();
  }

  @Override
  public int getRetweetCount() {
    if (this.data == null) {
      return 0;
    }
    return this.data.getPublicMetrics().getRetweetCount();
  }

  @Override
  public int getLikeCount() {
    if (this.data == null) {
      return 0;
    }
    return this.data.getPublicMetrics().getLikeCount();
  }

  @Override
  public int getReplyCount() {
    if (this.data == null) {
      return 0;
    }
    return this.data.getPublicMetrics().getReplyCount();
  }

  @Override
  public int getQuoteCount() {
    if (this.data == null) {
      return 0;
    }
    return this.data.getPublicMetrics().getQuoteCount();
  }

  @Override
  public String getInReplyToUserId() {
    if (this.data == null) {
      return null;
    }
    return this.data.getInReplyToUserId();
  }

  @Override
  public User getUser() {
    if (this.includes == null) {
      return null;
    }
    return this.includes.getUsers()[0];
  }

  @Override
  public String getAuthorId() {
    if (this.data == null) {
      return null;
    }
    return this.data.getAuthorId();
  }

  public LocalDateTime getCreatedAt() {
    if (this.data == null) {
      return null;
    }
    return this.data.getCreatedAt();
  }

  public List<ContextAnnotation> getContextAnnotations() {
    if (this.data == null) {
      return new ArrayList<>();
    }
    return this.data.getContextAnnotations();
  }

  @Override
  public TweetType getTweetType() {
    if (this.data == null || this.data.referencedTweets == null || this.data.referencedTweets.isEmpty()) {
      return TweetType.DEFAULT;
    } else if (this.data.getReferencedTweets().size() > 1
               && (this.data.getReferencedTweets().get(0).getType().equals(TweetType.RETWEETED)
                   || this.data.getReferencedTweets().get(1).getType().equals(TweetType.RETWEETED))) {
      return TweetType.RETWEETED;
    }
    return this.data.getReferencedTweets().get(0).getType();
  }

  @Getter
  @Setter
  private static class ReferencedTweetDTO {

    private TweetType type;
    private String    id;
  }

  @Getter
  @Setter
  private static class Includes {

    private UserV2.UserData[] users;
  }

  @Getter
  @Setter
  private static class TweetPublicMetricsDTO {

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
