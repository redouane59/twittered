package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.helpers.ConverterHelper;
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
  private JsonNode    entities;
  @JsonProperty("tweetCount")
  @JsonAlias({"statuses_count", "tweets_count"})
  private int         tweetCount;
  @JsonAlias("created_at")
  private String      dateOfCreation;
  private String      lastUpdate;
  private String      location;
  @JsonProperty("profile_image_url")
  private String      profileImageUrl;
  private boolean     following;

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
    return id.hashCode();
  }

  @Override
  public LocalDateTime getDateOfCreation() {
    return ConverterHelper.getDateFromTwitterString(dateOfCreation);
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
    return ConverterHelper.getDateFromTwitterString(lastUpdate);
  }
}
