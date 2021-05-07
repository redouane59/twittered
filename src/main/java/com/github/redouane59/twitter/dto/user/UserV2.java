package com.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetV2;
import com.github.redouane59.twitter.helpers.ConverterHelper;
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
    private UserPublicMetrics publicMetrics;
    @JsonProperty("pinned_tweet_id")
    private String            pinnedTweetId;
    private String            description;
    private String            lang;
    private boolean           isProtectedAccount;
    private boolean           following;

    @Getter
    @Setter
    public static class Includes {

      private TweetV2.TweetData[] tweets;
    }

    @Override
    public LocalDateTime getDateOfCreation() {
      return ConverterHelper.getDateFromTwitterString(this.createdAt);
    }

    @Override
    public int getFollowersCount() {
      return this.publicMetrics.getFollowersCount();
    }

    @Override
    public int getFollowingCount() {
      return this.publicMetrics.getFollowingCount();
    }

    @Override
    public int getTweetCount() {
      return this.publicMetrics.getTweetCount();
    }

    @Override
    public Tweet getPinnedTweet() {
      LOGGER.error("Enable to access the data from here");
      return null;
    }
  }

  @Override
  public String getId() {
    return this.data == null ? null : this.data.getId();
  }

  @Override
  public String getName() {
    return this.data == null ? null : this.data.getName();
  }

  @Override
  public String getDisplayedName() {
    return this.data == null ? null : this.data.getDisplayedName();
  }

  @Override
  public String getLocation() {
    return this.data.getLocation();
  }

  @Override
  public String getDescription() {
    return this.data.getDescription();
  }

  @Override
  public LocalDateTime getDateOfCreation() {
    return ConverterHelper.getDateFromTwitterDateV2(this.data.getCreatedAt());
  }

  @Override
  public int getFollowersCount() {
    return this.data.getPublicMetrics().getFollowersCount();
  }

  @Override
  public int getFollowingCount() {
    return this.data.getPublicMetrics().getFollowingCount();
  }

  @Override
  public int getTweetCount() {
    return this.data.getPublicMetrics().getTweetCount();
  }

  @Override
  public String getLang() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isProtectedAccount() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isFollowing() {
    return this.data.isFollowing();
  }

  @Override
  public boolean isVerified() {
    return this.data == null ? false : this.data.verified;
  }

  @Override
  public Tweet getPinnedTweet() {
    if (this.includes.getTweets().length < 1) {
      LOGGER.error("No tweet found");
      return null;
    }
    return this.includes.getTweets()[0];
  }

  @Override
  public String getUrl() {
    return this.data == null ? null : this.data.getUrl();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || (this.getClass() != o.getClass() && !User.class.isAssignableFrom(o.getClass()))) {
      return false;
    }
    User otherUser = (User) o;
    return (otherUser).getId().equals(this.getId());
  }

  @Override
  public int hashCode() {
    return this.getId().hashCode();
  }
}
