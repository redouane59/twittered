package io.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import java.time.LocalDateTime;

public interface User {

  /**
   * Get the string id of the user
   *
   * @return the user id
   */
  String getId();

  /**
   * Get the screen name of the user
   *
   * @return the user name
   */
  String getName();

  /**
   * Get the name of the user they’ve defined it on their profile. Not necessarily a person’s name. Typically capped at 50 characters, but subject to
   * change.
   *
   * @return the user name
   */
  String getDisplayedName();

  /**
   * Get the location of the user
   *
   * @return the user location
   */
  String getLocation();

  /**
   * Get the bio of a user
   *
   * @return the user description
   */
  String getDescription();

  /**
   * Get the date of creation of the user account
   *
   * @return the user account date of creation
   */
  LocalDateTime getDateOfCreation();

  /**
   * Get the number of followers of the user
   *
   * @return the user number of followers
   */
  int getFollowersCount();

  /**
   * Get the number of followings of the user
   *
   * @return the user number of followings
   */
  int getFollowingCount();

  /**
   * Get the number of tweets written by the user
   *
   * @return the user total number of tweets
   */
  int getTweetCount();

  /**
   * Get the language of the user
   *
   * @return the user language
   */
  String getLang();

  /**
   * Get the pinned tweet of the user
   *
   * @return the pinned tweet of the user
   */
  Tweet getPinnedTweet();

  /**
   * Get the url of the user profile
   *
   * @return The URL specified in the user's profile, if present.
   */
  String getUrl();

  /**
   * Get the profile image url
   */
  String getProfileImageUrl();

  /**
   * Get the entities user profile
   *
   * @return details about text that has a special meaning in the user's description.
   */
  JsonNode getEntities();

  /**
   * Get the protection level of the user account
   *
   * @return true if the account is protected, else false
   */
  boolean isProtectedAccount();

  /**
   * Get if the user is following the owner account. Warning: this is not not support by all endpoints.
   *
   * @return true if the user is following the owner account, else false
   */
  boolean isFollowing();

  /**
   * Get if the user has a verified account. Warning: this is not not support by all endpoints.
   *
   * @return true if the user account is certified
   */
  boolean isVerified();

}
