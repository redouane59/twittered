package com.github.redouane59.twitter;

import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.dto.others.RateLimitStatus;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import com.github.redouane59.twitter.dto.user.User;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface ITwitterClientV1 {

  /**
   * Like a tweet calling https://api.twitter.com/1.1/favorites/create.json
   *
   * @param tweetId the id of the tweet
   * @return the liked tweet
   */
  Tweet likeTweet(String tweetId);

  /**
   * Unlike a tweet calling https://api.twitter.com/1.1/favorites/destroy.json
   *
   * @param tweetId the id of the tweet
   * @return the unliked tweet
   */
  Tweet unlikeTweet(String tweetId);

  /**
   * Retweet a tweet
   *
   * @param tweetId the id of the tweet
   * @return the retweeted tweet
   */
  Tweet retweetTweet(String tweetId);

  /**
   * Post a tweet calling https://api.twitter.com/1.1/statuses/update.json
   *
   * @param text the tweet text
   * @return the created tweet
   */
  Tweet postTweet(String text);

  /**
   * Post a tweet calling https://api.twitter.com/1.1/statuses/update.json
   *
   * @param text the tweet text
   * @param inReplyToStatusId the id of the tweet to answer. Note: This parameter will be ignored unless the author of the Tweet this parameter
   * references is mentioned within the status text. Therefore, you must include @username , where username is the author of the referenced Tweet,
   * within the update.
   * @return the created tweet
   */
  Tweet postTweet(String text, String inReplyToStatusId);

  /**
   * Post a tweet calling with media https://api.twitter.com/1.1/statuses/update.json
   */
  Tweet postTweet(String text, String inReplyToStatusId, String mediaId);

  /**
   * Delete a tweet calling https://api.twitter.com/1.1/statuses/destroy/:id.json
   *
   * @param tweetId the id of the tweet
   * @return the deleted tweet
   */
  Tweet deleteTweet(String tweetId);

  /**
   * Get a list of ids of the users who retweeted a tweet calling https://api.twitter.com/1.1/statuses/retweeters/
   *
   * @param tweetId the id of the tweet
   * @return a list of the ids of the users who retweeted a tweet
   */
  List<String> getRetweetersId(String tweetId);

  /**
   * Get the relation between two users calling https://api.twitter.com/1.1/friendships/
   *
   * @param userId1 id of the first user
   * @param userId2 id of the second user
   * @return One of the following RelationType enum value : FRIENDS if the two users are following each other, NONE if they neither of the two is
   * following the other, FOLLOWER if user2 follows user1, FOLLOWING if user1 follows user2
   */
  RelationType getRelationType(String userId1, String userId2);

  /**
   * Follow a user calling https://api.twitter.com/1.1/friendships/
   *
   * @param userId the id of the user
   * @return the user
   */
  User follow(String userId);

  /**
   * Unfollow a user calling https://api.twitter.com/1.1/friendships/
   *
   * @param userId the id of the user
   * @return the user
   */
  User unfollow(String userId);

  /**
   * Unfollow a user calling https://api.twitter.com/1.1/friendships/
   *
   * @param userName the scree name of the user
   * @return the user
   */
  User unfollowByName(String userName);

  /**
   * Get the twitter rate limit status calling https://api.twitter.com/1.1/application/
   *
   * @return the twitter response
   */
  RateLimitStatus getRateLimitStatus();

  /**
   * Get the most recent Tweets liked calling https://api.twitter.com/1.1/favorites/list.json
   *
   * @param userId id of the user
   * @param count number of needed tweets
   * @return a list of liked tweets
   */
  List<Tweet> getFavorites(String userId, int count);

  /**
   * Search tweets from last 30 days calling https://api.twitter.com/1.1/tweets/search/30day/dev.json Your development environment name should be
   * "dev". See https://developer.twitter.com/en/account/environments
   *
   * @param query the search query
   * @param fromDate the start date
   * @param toDate the end date
   * @param envName name of the premium environment. See https://developer.twitter.com/en/account/environments
   * @return a list of tweets
   */
  List<Tweet> searchForTweetsWithin30days(String query, LocalDateTime fromDate, LocalDateTime toDate, String envName);

  /**
   * Search historic tweets calling https://api.twitter.com/1.1/tweets/search/fullarchive/dev.json Your development environment name should be "dev".
   * See https://developer.twitter.com/en/account/environments
   *
   * @param query the search query
   * @param fromDate the start date
   * @param toDate the end date
   * @param envName name of the premium environment. See https://developer.twitter.com/en/account/environments
   * @return a list of tweets
   */

  List<Tweet> searchForTweetsArchive(String query, LocalDateTime fromDate, LocalDateTime toDate, String envName);

  /**
   * Get token and secret token (oAuth1) calling https://api.twitter.com/oauth/request_token
   *
   * @return and object containing the oauth token and the oauth token secret
   */
  RequestToken getOauth1Token();

  /**
   * Get token and secret token (oAuth1) calling https://api.twitter.com/oauth/request_token
   *
   * @param oauthCallback the URL you wish your user to be redirected to when they complete the next step ("oob" to show the pincode)
   * @return and object containing the oauth token and the oauth token secret
   */
  RequestToken getOauth1Token(String oauthCallback);

  /**
   * Convert the request token into a usable access token calling https://api.twitter.com/oauth/access_token
   *
   * @param requestToken the token and secret token
   * @param pinCode the oauth verifier
   * @return the access tokens
   */
  RequestToken getOAuth1AccessToken(RequestToken requestToken, String pinCode);

  /**
   * Upload a media calling https://upload.twitter.com/1.1/media/upload.json
   */
  UploadMediaResponse uploadMedia(File image);
}

