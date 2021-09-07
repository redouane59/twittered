package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV2.TweetData;
import io.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @version V2
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UserV2 implements User {

  private UserData          data;
  private UserData.Includes includes;

  @Override
  public String getId() {
    return data == null ? null : data.getId();
  }

  @Override
  public String getName() {
    return data == null ? null : data.getName();
  }

  @Override
  public String getDisplayedName() {
    return data == null ? null : data.getDisplayedName();
  }

  @Override
  public String getLocation() {
    return data.getLocation();
  }

  @Override
  public String getDescription() {
    return data.getDescription();
  }

  @Override
  public LocalDateTime getDateOfCreation() {
    return ConverterHelper.getDateFromTwitterStringV2(data.getCreatedAt());
  }

  @Override
  @JsonIgnore
  public int getFollowersCount() {
    return data.getPublicMetrics().getFollowersCount();
  }

  @Override
  @JsonIgnore
  public int getFollowingCount() {
    return data.getPublicMetrics().getFollowingCount();
  }

  @Override
  @JsonIgnore
  public int getTweetCount() {
    return data.getPublicMetrics().getTweetCount();
  }

  @Override
  @JsonIgnore
  public String getLang() {
    throw new UnsupportedOperationException();
  }

  @Override
  @JsonIgnore
  public boolean isProtectedAccount() {
    return data != null && data.protectedAccount;
  }

  @Override
  public boolean isFollowing() {
    return data.isFollowing();
  }

  @Override
  public boolean isVerified() {
    return data != null && data.verified;
  }

  @Override
  public Tweet getPinnedTweet() {
    if (includes == null || includes.getTweets() == null || includes.getTweets().length < 1) {
      LOGGER.error("No tweet found");
      return null;
    }
    return includes.getTweets()[0];
  }

  @Override
  public String getUrl() {
    return data == null ? null : data.getUrl();
  }

  @Override
  public String getProfileImageUrl() {
    return data == null ? null : data.profileImageUrl;
  }

  @Override
  public JsonNode getEntities() {
    return data == null ? null : data.getEntities();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || (getClass() != o.getClass() && !User.class.isAssignableFrom(o.getClass()))) {
      return false;
    }
    User otherUser = (User) o;
    return (otherUser).getId().equals(getId());
  }

  @Override
  public int hashCode() {
    return getId().hashCode();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UserData implements User {

    private String            id;
    @JsonProperty("created_at")
    private String            createdAt;
    @JsonProperty("username")
    private String            name;
    @JsonProperty("name")
    private String            displayedName;
    private String            location;
    private JsonNode          entities;
    private String            url;
    private boolean           verified;
    @JsonProperty("profile_image_url")
    private String            profileImageUrl;
    @JsonProperty("public_metrics")
    @JsonInclude(Include.NON_NULL)
    private UserPublicMetrics publicMetrics;
    @JsonProperty("pinned_tweet_id")
    private String            pinnedTweetId;
    private String            description;
    private String            lang;
    @JsonProperty("protected")
    private boolean           protectedAccount;
    private boolean           following;

    @Override
    public LocalDateTime getDateOfCreation() {
      return ConverterHelper.getDateFromTwitterStringV2(createdAt);
    }

    @Override
    public int getFollowersCount() {
      return publicMetrics.getFollowersCount();
    }

    @Override
    public int getFollowingCount() {
      return publicMetrics.getFollowingCount();
    }

    @Override
    public int getTweetCount() {
      return publicMetrics.getTweetCount();
    }

    @Override
    @JsonIgnore
    public Tweet getPinnedTweet() {
      LOGGER.error("Enable to access the data from here");
      return null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Includes {

      private TweetData[] tweets;
    }
  }
}
