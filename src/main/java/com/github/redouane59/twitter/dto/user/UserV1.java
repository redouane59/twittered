package com.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @version V1
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class UserV1 implements User {

  private String      id;
  @JsonProperty("screen_name")
  @JsonAlias("screen_name")
  private String      name;
  @JsonAlias("name")
  private String      displayedName;
  private List<Tweet> mostRecentTweet;
  private String      description;
  @JsonAlias("protected")
  private boolean     protectedAccount;
  @JsonProperty("followers_count")
  private int         followersCount;
  @JsonProperty("friends_count")
  @JsonAlias({"friends_count", "followings_count"})
  private int         followingCount;
  private String      lang;
  private String      url;
  @JsonProperty("tweetCount")
  @JsonAlias({"statuses_count", "tweets_count"})
  private int         tweetCount;
  @JsonAlias("created_at")
  private String      dateOfCreation;
  private String      lastUpdate;
  private String      location;
  private boolean     following;

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
    return this.id.hashCode();
  }

  public LocalDateTime getDateOfCreation() {
    return ConverterHelper.getDateFromTwitterString(this.dateOfCreation);
  }

  @Override
  public Tweet getPinnedTweet() {
    LOGGER.debug("UnsupportedOperation");
    return null;
  }

  @Override
  public boolean isVerified() {
    LOGGER.debug("UnsupportedOperation");
    return false;
  }

  public LocalDateTime getLastUpdate() {
    return ConverterHelper.getDateFromTwitterString(this.lastUpdate);
  }
}
