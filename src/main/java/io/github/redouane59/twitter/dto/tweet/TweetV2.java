package io.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.stream.StreamRules;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.entities.BaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.Entities;
import io.github.redouane59.twitter.dto.tweet.entities.HashtagEntity;
import io.github.redouane59.twitter.dto.tweet.entities.MediaEntity;
import io.github.redouane59.twitter.dto.tweet.entities.SymbolEntity;
import io.github.redouane59.twitter.dto.tweet.entities.TextBaseEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UrlEntity;
import io.github.redouane59.twitter.dto.tweet.entities.UserMentionEntity;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserV2;
import io.github.redouane59.twitter.dto.user.UserV2.UserData;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.Collections;
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

  private static final String                       NOT_IMPLEMENTED_EXCEPTION = "not implemented";
  private              TweetData                    data;
  private              Includes                     includes;
  @JsonProperty("matching_rules")
  private              List<StreamRules.StreamRule> matchingRules;

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
  public List<MediaEntityV2> getMedia() {
    if (includes == null) {
      return Collections.emptyList();
    }
    return includes.getMedia();
  }

  @Override
  public List<Place> getPlaces() {
    if (includes == null) {
      return Collections.emptyList();
    }
    return includes.getPlaces();
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
  public int getImpressionCount() {
    if (data == null || data.getNonPublicMetrics() == null) {
        return 0;
    }
    return data.getNonPublicMetrics().getImpressionCount();
  }

  @Override
  public int getUrlLinkClicks() {
    if (data == null || data.getNonPublicMetrics() == null) {
        return 0;
    }
    return data.getNonPublicMetrics().getUrlLinkClicks();
  }

  @Override
  public int getUserProfileClicks() {
    if (data == null || data.getNonPublicMetrics() == null) {
        return 0;
    }
    return data.getNonPublicMetrics().getUserProfileClicks();
  }

  @Override
  public int getOrganicImpressionCount() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getImpressionCount();
  }

  @Override
  public int getOrganicLikeCount() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getLikeCount();
  }

  @Override
  public int getOrganicReplyCount() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getReplyCount();
  }

  @Override
  public int getOrganicRetweetCount() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getRetweetCount();
  }

  @Override
  public int getOrganicUrlLinkClicks() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getUrlLinkClicks();
  }

  @Override
  public int getOrganicUserProfileClicks() {
    if (data == null || data.getOrganicMetrics() == null) {
        return 0;
    }
    return data.getOrganicMetrics().getUserProfileClicks();
  }

  @Override
  public int getPromotedImpressionCount() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getImpressionCount();
  }

  @Override
  public int getPromotedLikeCount() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getLikeCount();
  }

  @Override
  public int getPromotedReplyCount() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getReplyCount();
  }

  @Override
  public int getPromotedRetweetCount() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getRetweetCount();
  }

  @Override
  public int getPromotedUrlLinkClicks() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getUrlLinkClicks();
  }

  @Override
  public int getPromotedUserProfileClicks() {
    if (data == null || data.getPromotedMetrics() == null) {
        return 0;
    }
    return data.getPromotedMetrics().getUserProfileClicks();
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
      return Collections.emptyList();
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

  /** Returns whether the TweetPublicMetricsDTO object exists. Probably the field public_metrics was not requested if not. */
  public boolean hasTweetPublicMetrics() {
    return data!=null && data.hasPublicMetrics();
  }

  /** Returns whether the TweetNonPublicMetricsDTO object exists. Probably the field non_public_metrics was not requested if not. */
  public boolean hasTweetNonPublicMetrics() {
    return data!=null && data.hasNonPublicMetrics();
  }
  
  /** Returns whether the TweetOrganicMetricsDTO object exists. Probably the field organic_metrics was not requested if not. */
  public boolean hasTweetOrganicMetrics() {
    return data!=null && data.hasOrganicMetrics();
  }
  
  /** Returns whether the TweetPromotedMetricsDTO object exists. Probably the field promoted_metrics was not requested if not. */
  public boolean hasTweetPromotedMetrics() {
    return data!=null && data.hasPromotedMetrics();
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
    private EntitiesV2               entities;
    @JsonProperty("public_metrics")
    @JsonInclude(Include.NON_NULL)
    private TweetPublicMetricsDTO    publicMetrics;
    @JsonProperty("non_public_metrics")
    @JsonInclude(Include.NON_NULL)
    private TweetNonPublicMetricsDTO nonPublicMetrics;
    @JsonProperty("organic_metrics")
    @JsonInclude(Include.NON_NULL)
    private TweetOrganicMetricsDTO   organicMetrics;
    @JsonProperty("promoted_metrics")
    @JsonInclude(Include.NON_NULL)
    private TweetPromotedMetricsDTO  promotedMetrics;
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
    @JsonIgnore
    private UserData                 user;

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

    /** Returns whether the TweetPublicMetricsDTO object exists. Probably the field public_metrics was not requested if not. */
    public boolean hasPublicMetrics() {
      return publicMetrics!=null;
    }

    @Override
    @JsonIgnore
    public int getImpressionCount() {
      return nonPublicMetrics.getImpressionCount();
    }

    @Override
    @JsonIgnore
    public int getUrlLinkClicks() {
      return nonPublicMetrics.getUrlLinkClicks();
    }
    @Override
    @JsonIgnore
    public int getUserProfileClicks() {
      return nonPublicMetrics.getUserProfileClicks();
    }

    /** Returns whether the TweetNonPublicMetricsDTO object exists. Probably the field non_public_metrics was not requested if not. */
    public boolean hasNonPublicMetrics() {
      return nonPublicMetrics!=null;
    }

    @Override
    @JsonIgnore
    public int getOrganicImpressionCount() {
      return organicMetrics.getImpressionCount();
    }

    @Override
    @JsonIgnore
    public int getOrganicLikeCount() {
      return organicMetrics.getLikeCount();
    }

    @Override
    @JsonIgnore
    public int getOrganicReplyCount() {
      return organicMetrics.getReplyCount();
    }

    @Override
    @JsonIgnore
    public int getOrganicRetweetCount() {
      return organicMetrics.getRetweetCount();
    }

    @Override
    @JsonIgnore
    public int getOrganicUrlLinkClicks() {
      return organicMetrics.getUrlLinkClicks();
    }

    @Override
    @JsonIgnore
    public int getOrganicUserProfileClicks() {
      return organicMetrics.getUserProfileClicks();
    }
    
    /** Returns whether the TweetOrganicMetricsDTO object exists. Probably the field organic_metrics was not requested if not. */
    public boolean hasOrganicMetrics() {
      return organicMetrics!=null;
    }

    @Override
    @JsonIgnore
    public int getPromotedImpressionCount() {
      return promotedMetrics.getImpressionCount();
    }

    @Override
    @JsonIgnore
    public int getPromotedLikeCount() {
      return promotedMetrics.getLikeCount();
    }

    @Override
    @JsonIgnore
    public int getPromotedReplyCount() {
      return promotedMetrics.getReplyCount();
    }

    @Override
    @JsonIgnore
    public int getPromotedRetweetCount() {
      return promotedMetrics.getRetweetCount();
    }

    @Override
    @JsonIgnore
    public int getPromotedUrlLinkClicks() {
      return promotedMetrics.getUrlLinkClicks();
    }

    @Override
    @JsonIgnore
    public int getPromotedUserProfileClicks() {
      return promotedMetrics.getUserProfileClicks();
    }

    /** Returns whether the TweetPromotedMetricsDTO object exists. Probably the field promoted_metrics was not requested if not. */
    public boolean hasPromotedMetrics() {
      return promotedMetrics!=null;
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
    public List<MediaEntityV2> getMedia() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return Collections.emptyList();
    }

    @Override
    public List<Place> getPlaces() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return Collections.emptyList();
    }

    @Override
    public List<StreamRule> getMatchingRules() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return Collections.emptyList();
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

    private List<UserV2.UserData>       users;
    private List<TweetV2.TweetData>     tweets;
    private List<TweetV2.MediaEntityV2> media;
    private List<TweetV2.Place>         places;
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

  /** Non-public engagement metrics for the Tweet at the time of the request. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication.
   * To return this field, add tweet.fields=non_public_metrics in the request's query parameter.*/
  @Getter
  @Setter
  public static class TweetNonPublicMetricsDTO {

    /** Number of times the Tweet has been viewed. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. **/
    @JsonProperty("impression_count")
    private int impressionCount;
    /** Number of times a user clicks on a URL link or URL preview card in a Tweet. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("url_link_clicks")
    private int urlLinkClicks;
    /** Number of times a user clicks the following portions of a Tweet - display name, user name, profile picture. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("user_profile_clicks")
    private int userProfileClicks;
  }

  /** Organic engagement metrics for the Tweet at the time of the request. Requires user context authentication. */
  @Getter
  @Setter
  public static class TweetOrganicMetricsDTO {

    /** Number of times the Tweet has been viewed organically. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("impression_count")
    private int impressionCount;
    /** Number of times a user clicks on a URL link or URL preview card in a Tweet organically. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("url_link_clicks")
    private int urlLinkClicks;
    /** Number of times a user clicks the following portions of a Tweet organically - display name, user name, profile picture. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("user_profile_clicks")
    private int userProfileClicks;
    /** Number of times the Tweet has been Retweeted organically. */
    @JsonProperty("retweet_count")
    private int retweetCount;
    /** Number of replies the Tweet has received organically. */
    @JsonProperty("reply_count")
    private int replyCount;
    /** Number of likes the Tweet has received organically. */
    @JsonProperty("like_count")
    private int likeCount;

  }

  /** Engagement metrics for the Tweet at the time of the request in a promoted context. Requires user context authentication. */
  @Getter
  @Setter
  public static class TweetPromotedMetricsDTO {

    /** Number of times the Tweet has been viewed when that Tweet is being promoted. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("impression_count")
    private int impressionCount;
    /** Number of times a user clicks on a URL link or URL preview card in a Tweet when it is being promoted. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("url_link_clicks")
    private int urlLinkClicks;
    /** Number of times a user clicks the following portions of a Tweet when it is being promoted - display name, user name, profile picture. This is a private metric, and requires the use of OAuth 1.0a or OAuth 2.0 User Context authentication. */
    @JsonProperty("user_profile_clicks")
    private int userProfileClicks;
    /** Number of times this Tweet has been Retweeted when that Tweet is being promoted. */
    @JsonProperty("retweet_count")
    private int retweetCount;
    /** Number of Replies to this Tweet when that Tweet is being promoted. */
    @JsonProperty("reply_count")
    private int replyCount;
    /** Number of Likes of this Tweet when that Tweet is being promoted. */
    @JsonProperty("like_count")
    private int likeCount;

  }
  
  @Getter
  @Setter
  public static class EntitiesV2 implements Entities {

    private List<HashtagEntityV2>     hashtags;
    private List<UrlEntityV2>         urls;
    @JsonProperty("mentions")
    private List<UserMentionEntityV2> userMentions;
    @JsonProperty("cashtags")
    private List<CashtagEntityV2>     symbols;

  }

  @Getter
  @Setter
  public static class BaseEntityV2 implements BaseEntity {

    private int start;
    private int end;
  }

  @Getter
  @Setter
  public static class TextBaseEntityV2 extends BaseEntityV2 implements TextBaseEntity {

    private String tag;

    @Override
    public String getText() {
      return tag;
    }
  }

  @Getter
  @Setter
  public static class UrlEntityV2 extends BaseEntityV2 implements UrlEntity {

    private String url;
    @JsonProperty("display_url")
    private String displayUrl;
    @JsonProperty("expanded_url")
    private String expandedUrl;
    private int    status;
    private String description;
    private String title;
    @JsonProperty("unwound_url")
    private String unwoundedUrl;
  }

  @Getter
  @Setter
  public static class HashtagEntityV2 extends TextBaseEntityV2 implements HashtagEntity {

  }

  @Getter
  @Setter
  public static class UserMentionEntityV2 extends TextBaseEntityV2 implements UserMentionEntity {

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
  public static class CashtagEntityV2 extends TextBaseEntityV2 implements SymbolEntity {

  }

  @Getter
  @Setter
  public static class MediaEntityV2 implements MediaEntity {

    @JsonProperty("media_key")
    private String                key;
    private String                type;
    @JsonProperty("duration_ms")
    private int                   duration;
    private int                   height;
    private int                   width;
    private String                url;
    @JsonProperty("preview_image_url")
    private String                previewImageUrl;
    @JsonProperty("public_metrics")
    private MediaPublicMetricsDTO publicMetrics;
    @JsonProperty("alt_text")
    private String                altText;

    @Override
    public int getStart() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return -1;
    }

    @Override
    public int getEnd() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return -1;
    }

    @Override
    public String getDisplayUrl() {
      return getUrl();
    }

    @Override
    public String getExpandedUrl() {
      return getUrl();
    }

    @Override
    public String getMediaUrl() {
      return getUrl();
    }

    @Override
    public long getId() {
      LOGGER.info(NOT_IMPLEMENTED_EXCEPTION);
      return -1;
    }
  }

  @Getter
  @Setter
  public static class Place {

    private Geo    geo;
    @JsonProperty("country_code")
    private String countryCode;
    private String name;
    private String id;
    @JsonProperty("place_type")
    private String placeType;
    private String country;
    @JsonProperty("full_name")
    private String fullName;

    @Getter
    @Setter
    public static class Geo {

      private String       type;
      private List<Double> bbox;
      private JsonNode     properties;
    }
  }

  @Getter
  @Setter
  public static class MediaPublicMetricsDTO {

    @JsonProperty("view_count")
    private int viewCount;
  }
}
