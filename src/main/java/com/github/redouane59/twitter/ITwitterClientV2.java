package com.github.redouane59.twitter;

import com.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import com.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetSearchResponse;
import com.github.redouane59.twitter.dto.user.User;
import java.time.LocalDateTime;
import java.util.List;
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
  List<Tweet> getTweets(List<String> tweetIds);

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
   * @param fromDate the start date
   * @param toDate the end date
   * @return a list of tweets
   */
  List<Tweet> searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate);

  /**
   * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
   *
   * @param query the search query
   * @return a list of tweets
   */
  List<Tweet> searchForTweetsWithin7days(String query);

  /**
   * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
   *
   * @param query the search query
   * @param maxResult maximum 100 (default 10)
   * @param nextToken the next_token given by the API to start from an index
   * @return a TweetSearchResponse object containing a list of tweets and the next token
   */
  TweetSearchResponse searchForTweetsWithin7days(String query, int maxResult, String nextToken);


  /**
   * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
   *
   * @param query the search query
   * @param fromDate the start date
   * @param toDate the end date
   * @param maxResult maximum 100 (default 10)
   * @param nextToken the next_token given by the API to start from an index
   * @return a TweetSearchResponse object containing a list of tweets and the next token
   */
  TweetSearchResponse searchForTweetsWithin7days(String query, LocalDateTime fromDate, LocalDateTime toDate, int maxResult, String nextToken);

  /**
   * Stream using previous set up filters calling https://api.twitter.com/2/tweets/search/stream
   */
  void startFilteredStream(Consumer<Tweet> consumer);

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
  void startSampledStream(Consumer<Tweet> consumer);

}

