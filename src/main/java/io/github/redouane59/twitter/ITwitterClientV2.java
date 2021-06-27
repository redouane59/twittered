package io.github.redouane59.twitter;

import com.github.scribejava.core.model.Response;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.others.BlockResponse;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.LikeResponse;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetCountsList;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.user.FollowResponse;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface ITwitterClientV2 {

  /**
   * Retreive a user from his screen name calling https://api.twitter.com/2/users/
   *
   * @param userName the name of the targeted user
   * @return an user object related to the targeted user
   */
  User getUserFromUserName(String userName);

  /**
   * Retreive a user from his id calling https://api.twitter.com/2/users/
   *
   * @param userId the id of the user
   * @return an user object related to the targeted user
   */
  User getUserFromUserId(String userId);

  /**
   * Retreive a list of users from their usernames calling https://api.twitter.com/2/users/
   *
   * @param userNames the names of the targeted user
   * @return an list of user objects related to the targeted users
   */
  List<User> getUsersFromUserNames(List<String> userNames);

  /**
   * Retreive a list of users from their ids calling https://api.twitter.com/2/users/
   *
   * @param userIds the id of the user
   * @return an list of user object related to the targeted users
   */
  List<User> getUsersFromUserIds(List<String> userIds);

  /**
   * Get a list of the user followers limited to 1000 results calling https://api.twitter.com/2/users/:id/followers
   *
   * @param userId the id of the targeted user
   * @return a list of users who are following the targeted user
   */
  UserList getFollowers(String userId);

  /**
   * Get a list of the user followers limited to 1000 results calling https://api.twitter.com/2/users/:id/followers
   *
   * @param userId the id of the targeted user
   * @param additionalParameters accepted parameters are maxResults, pagination_token
   * @return a list of users who are following the targeted user
   */
  UserList getFollowers(String userId, AdditionalParameters additionalParameters);

  /**
   * Get a list of the user following limited to 1000 results calling https://api.twitter.com/2/users/:id/following
   *
   * @param userId the id of the targeted user
   * @return a list of users that the targeted user is following
   */
  UserList getFollowing(String userId);

  /**
   * Get a list of the user following limited to 1000 results calling https://api.twitter.com/2/users/:id/following
   *
   * @param additionalParameters accepted parameters are maxResults, pagination_token
   * @param userId the id of the targeted user
   * @return a list of users that the targeted user is following
   */
  UserList getFollowing(String userId, AdditionalParameters additionalParameters);

  /**
   * Get a tweet from its id calling https://api.twitter.com/2/tweets
   *
   * @param tweetId id of the tweet
   * @return a tweet object
   */
  Tweet getTweet(String tweetId);

  /**
   * Get a tweet list from their id calling https://api.twitter.com/2/tweets
   *
   * @param tweetIds the ids of the tweets
   * @return a tweet object list
   */
  TweetList getTweets(List<String> tweetIds);

  /**
   * Hide/Unide a reply using https://api.twitter.com/labs/2/tweets/:id/hidden
   *
   * @param tweetId id of the concerned reply
   * @param hide true to hide the reply, false to unide it
   * @return the hidden state
   */
  boolean hideReply(String tweetId, boolean hide);

  /**
   * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
   *
   * @param query the search query
   * @return a TweetList object containing a list of tweets
   */
  TweetList searchTweets(String query);

  /**
   * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
   *
   * @param query the search query
   * @param additionalParameters accepted parameters are recursiveCall, startTime, endTime, sinceId, untilId, maxResults
   * @return a TweetList object containing a list of tweets and the next token if recursiveCall is set to false
   */
  TweetList searchTweets(String query, AdditionalParameters additionalParameters);

  /**
   * Search archived tweets calling https://api.twitter.com/2/tweets/search/all
   *
   * @param query the search query
   * @return a TweetList object containing a list of tweets
   */
  TweetList searchAllTweets(String query);

  /**
   * Search archived tweets calling https://api.twitter.com/2/tweets/search/all
   *
   * @param query the search query
   * @param additionalParameters accepted parameters are recursiveCall, startTime, endTime, sinceId, untilId, maxResults, nextToken
   * @return a TweetList object containing a list of tweets and the next token if recursiveCall is set to false
   */
  TweetList searchAllTweets(String query, AdditionalParameters additionalParameters);

  /**
   * Stream using previous set up filters calling https://api.twitter.com/2/tweets/search/stream
   */
  Future<Response> startFilteredStream(IAPIEventListener listener);

  /**
   * Stream using previous set up filters calling https://api.twitter.com/2/tweets/search/stream
   */
  Future<Response> startFilteredStream(Consumer<Tweet> tweet);

  /**
   * Stops the filtered stream with the result of the startFilteredStream. It'll close the socket opened.
   *
   * @param response Future<Response> given by startFilteredStream
   */
  boolean stopFilteredStream(Future<Response> response);

  /**
   * add a filtered stream rule calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param value the rule value
   * @param tag the rule associated tag
   * @return the created rules
   */
  StreamRule addFilteredStreamRule(String value, String tag);

  /**
   * Delete a filtered stream rule calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param ruleValue the value of the rule to delete
   * @return a StreamMeta object resuming the operation
   */
  StreamMeta deleteFilteredStreamRule(String ruleValue);

  /**
   * Delete a filtered stream from its rule tag calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param ruleTag the tag name specified when using addFilteredStreamRule
   * @return a StreamMeta object resuming the operation
   */
  StreamMeta deleteFilteredStreamRuletag(String ruleTag);

  /**
   * Retrieve the filtered stream rules calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @return a filtered stream rules list
   */
  List<StreamRule> retrieveFilteredStreamRules();

  /**
   * Get a bearer token (oAuth2) calling https://api.twitter.com/oauth2/token
   *
   * @return the generated token
   */
  String getBearerToken();

  /**
   * Stream about 1% of all tweets calling https://api.twitter.com/2/tweets/sample/stream
   */
  Future<Response> startSampledStream(Consumer<Tweet> consumer);

  /**
   * Stream about 1% of all tweets calling https://api.twitter.com/2/tweets/sample/stream
   */
  Future<Response> startSampledStream(IAPIEventListener listener);

  /**
   * Get the most recent Tweets posted by the user calling https://api.twitter.com/2/users/:id/tweets
   *
   * @param userId Unique identifier of the Twitter account (user ID) for whom to return results.
   * @return a TweetList object containing a list of tweets
   */
  TweetList getUserTimeline(String userId);

  /**
   * Get the most recent Tweets posted by the user calling https://api.twitter.com/2/users/:id/tweets (time & tweet id arguments can be null)
   *
   * @param userId identifier of the Twitter account (user ID) for whom to return results.
   * @param additionalParameters accepted parameters recursiveCall, startTime, endTime, sinceId, untilId, maxResults
   * @return a TweetList object containing a list of tweets and the next token if recursiveCall is set to false
   */
  TweetList getUserTimeline(String userId, AdditionalParameters additionalParameters);

  /**
   * Get the most recent mentions received posted by the user calling https://api.twitter.com/2/users/:id/mentions
   *
   * @return a TweetList object containing a list of tweets
   */
  TweetList getUserMentions(String userId);

  /**
   * Get the most recent mentions received by the user calling https://api.twitter.com/2/users/:id/mentions (time & tweet id arguments can be null)
   *
   * @param additionalParameters accepted parameters recursiveCall, startTime, endTime, sinceId, untilId, maxResults
   * @return a TweetList object containing a list of tweets and the next token if recursiveCall is set to false
   */
  TweetList getUserMentions(String userId, AdditionalParameters additionalParameters);

  /**
   * Follow a user calling https://api.twitter.com/2/users/:source_user_id/following
   *
   * @param targetUserId The user ID of the user that you would like the authenticated user to follow.
   * @return the follow information
   */
  FollowResponse follow(String targetUserId);

  /**
   * Unfollow a user calling https://api.twitter.com/2/users/:source_user_id/following/:target_user_id
   *
   * @param targetUserId The user ID of the user that you would like the authenticated user to unfollow.
   * @return the follow information
   */
  FollowResponse unfollow(String targetUserId);

  /**
   * Block a given user calling https://api.twitter.com/2/users/:id/blocking
   *
   * @param targetUserId The user ID of the user that you would like the id to block.
   * @return whether the user is blocking the specified user as a result of this request.
   */
  BlockResponse blockUser(String targetUserId);

  /**
   * Unblock a given user calling https://api.twitter.com/users/:source_user_id/blocking/:target_user_id
   *
   * @param targetUserId The user ID of the user that you would like the authenticated user to unblock.
   * @return Indicates whether the user is blocking the specified user as a result of this request. The returned value is false for a successful
   * unblock request.
   */
  BlockResponse unblockUser(String targetUserId);

  /**
   * Returns a list of users who are blocked by the authenticated user.
   */
  UserList getBlockedUsers();

  /**
   * Like a tweet calling https://api.twitter.com/2/users/:id/likes
   *
   * @param tweetId The ID of the Tweet that you would like the user id to Like. you must pass the Access Tokens associated with the user ID when
   * authenticating your request.
   * @return whether the user likes the specified Tweet as a result of this request.
   */
  LikeResponse likeTweet(String tweetId);

  /**
   * Unlike a tweet calling https://api.twitter.com/2/users/:id/likes/:tweet_id
   *
   * @param tweetId The ID of the Tweet that you would like the user id to Like. you must pass the Access Tokens associated with the user ID when
   * authenticating your request.
   * @return whether the user likes the specified Tweet as a result of this request.
   */
  LikeResponse unlikeTweet(String tweetId);

  /**
   * Allows you to get information about a Tweet’s liking users calling https://api.twitter.com/2/tweets/:id/liking_users
   *
   * @param tweetId ID of the Tweet to request liking users of.
   */
  UserList getLikingUsers(String tweetId);

  /**
   * Allows you to get information about a user’s liked Tweets calling https://api.twitter.com/2/users/:id/liked_tweets
   *
   * @param userId ID of the user to request liked Tweets for.
   * @return a TweetList object containing a list of tweets and the next token
   */
  TweetList getLikedTweets(String userId);

  /**
   * Allows you to get information about a user’s liked Tweets calling https://api.twitter.com/2/users/:id/liked_tweets
   *
   * @param userId ID of the user to request liked Tweets for.
   * @param additionalParameters accepted parameters are maxResults, paginationToken,
   * @return a TweetList object containing a list of tweets and the next token
   */
  TweetList getLikedTweets(String userId, AdditionalParameters additionalParameters);


  /**
   * The recent Tweet counts endpoint returns count of Tweets from the last seven days that match a search query calling
   * https://api.twitter.com/2/tweets/counts/recent
   *
   * @param query One rule for matching Tweets
   */
  TweetCountsList getTweetCounts(String query);

  /**
   * The recent Tweet counts endpoint returns count of Tweets from the last seven days that match a search query calling
   * https://api.twitter.com/2/tweets/counts/recent
   *
   * @param query One rule for matching Tweets
   * @param additionalParameters accepted parameters are startTime, endTime, sinceId, untilId, and granularity
   */
  TweetCountsList getTweetCounts(String query, AdditionalParameters additionalParameters);

  /**
   * The full-archive search endpoint returns the complete history of public Tweets matching a search query; since the first Tweet was created March
   * 26, 2006 calling https://api.twitter.com/2/tweets/counts/all
   *
   * @param query One query for matching Tweets.
   */
  TweetCountsList getAllTweetCounts(String query);

  /**
   * The full-archive search endpoint returns the complete history of public Tweets matching a search query; since the first Tweet was created March
   * 26, 2006 calling https://api.twitter.com/2/tweets/counts/all
   *
   * @param query One query for matching Tweets.
   * @param additionalParameters accepted parameters are startTime, endTime, sinceId, untilId, granularity and nextToken
   */
  TweetCountsList getAllTweetCounts(String query, AdditionalParameters additionalParameters);

}

