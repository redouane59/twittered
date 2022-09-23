package io.github.redouane59.twitter;

import com.github.scribejava.core.model.Response;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.list.TwitterList;
import io.github.redouane59.twitter.dto.list.TwitterListList;
import io.github.redouane59.twitter.dto.others.BearerToken;
import io.github.redouane59.twitter.dto.others.BlockResponse;
import io.github.redouane59.twitter.dto.rules.FilteredStreamRulePredicate;
import io.github.redouane59.twitter.dto.space.Space;
import io.github.redouane59.twitter.dto.space.SpaceList;
import io.github.redouane59.twitter.dto.space.SpaceState;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamMeta;
import io.github.redouane59.twitter.dto.stream.StreamRules.StreamRule;
import io.github.redouane59.twitter.dto.tweet.LikeResponse;
import io.github.redouane59.twitter.dto.tweet.RetweetResponse;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetCountsList;
import io.github.redouane59.twitter.dto.tweet.TweetList;
import io.github.redouane59.twitter.dto.tweet.TweetParameters;
import io.github.redouane59.twitter.dto.user.User;
import io.github.redouane59.twitter.dto.user.UserActionResponse;
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
  Future<Response> startFilteredStream(Consumer<Tweet> tweet);

  /**
   * Stream using previous set up filters calling https://api.twitter.com/2/tweets/search/stream
   */
  Future<Response> startFilteredStream(IAPIEventListener listener);

  /**
   * Stream using previous set up filters calling https://api.twitter.com/2/tweets/search/stream
   *
   * @param backfillMinutes By passing this parameter, you can recover up to five minutes worth of data that you might have missed during a
   * disconnection. The backfilled Tweets will automatically flow through a reconnected stream, with older Tweets generally being delivered before any
   * newly matching Tweets. You must include a whole number between 1 and 5 as the value to this parameter.
   */
  Future<Response> startFilteredStream(IAPIEventListener listener, int backfillMinutes);

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
   * add a filtered stream rule calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param value the rule value
   * @param tag the rule associated tag
   * @return the created rules
   */
  StreamRule addFilteredStreamRule(FilteredStreamRulePredicate value, String tag);

  /**
   * Delete a filtered stream rule calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param ruleValue the value of the rule to delete
   * @return a StreamMeta object resuming the operation
   */
  StreamMeta deleteFilteredStreamRule(FilteredStreamRulePredicate ruleValue);

  /**
   * Delete a filtered stream rule calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param ruleValue the value of the rule to delete
   * @return a StreamMeta object resuming the operation
   */
  StreamMeta deleteFilteredStreamRule(String ruleValue);

  /**
   * Delete a filtered stream from its rule id calling https://api.twitter.com/2/tweets/search/stream/rules
   *
   * @param ruleId the id returned when using retrieveFilteredStreamRules
   * @return a StreamMeta object resuming the operation
   */
  StreamMeta deleteFilteredStreamRuleId(String ruleId);

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
   * Refresh tokens allow an application to obtain a new access token without prompting the user via the refresh token flow.
   *
   * If the scope offline.access is applied an OAuth 2.0 refresh token will be issued. With this refresh token, you obtain an access token. If this
   * scope is not passed, we will not generate a refresh token.
   */
  BearerToken getOAuth2RefreshToken(String refreshToken, String clientId);

  /**
   * Use the authorization code to create an access token. See https://developer.twitter.com/en/docs/authentication/oauth-2-0/user-access-token
   *
   * @param clientId Can be found in the developer portal under the header "Client ID".
   * @param code the authorization code obtained previously from the user redirection url
   * @param codeVerifier the code set before when calling method getAuthorizeUrl()
   * @param redirectUri the redirect URI set before when calling method getAuthorizeUrl()
   */
  BearerToken getOAuth2AccessToken(String clientId, String code, String codeVerifier, String redirectUri);

  /**
   * Stream about 1% of all tweets calling https://api.twitter.com/2/tweets/sample/stream
   */
  Future<Response> startSampledStream(Consumer<Tweet> consumer);

  /**
   * Stream about 1% of all tweets calling https://api.twitter.com/2/tweets/sample/stream
   */
  Future<Response> startSampledStream(IAPIEventListener listener);

  /**
   * Stream about 1% of all tweets calling https://api.twitter.com/2/tweets/sample/stream
   *
   * @param backfillMinutes By passing this parameter, you can recover up to five minutes worth of data that you might have missed during a
   * disconnection. The backfilled Tweets will automatically flow through a reconnected stream, with older Tweets generally being delivered before any
   * newly matching Tweets. You must include a whole number between 1 and 5 as the value to this parameter.
   */
  Future<Response> startSampledStream(IAPIEventListener listener, int backfillMinutes);

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
  UserActionResponse follow(String targetUserId);

  /**
   * Unfollow a user calling https://api.twitter.com/2/users/:source_user_id/following/:target_user_id
   *
   * @param targetUserId The user ID of the user that you would like the authenticated user to unfollow.
   * @return the follow information
   */
  UserActionResponse unfollow(String targetUserId);

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
   * Allows you to get information about a Tweet’s all liking users calling https://api.twitter.com/2/tweets/:id/liking_users
   *
   * @param tweetId ID of the Tweet to request liking users of.
   */
  UserList getLikingUsers(String tweetId);

  /**
   * Allows you to get information about a Tweet’s liking users calling https://api.twitter.com/2/tweets/:id/liking_users
   *
   * @param tweetId ID of the Tweet to request liking users of.
   * @param maxResults the maximum results to be returned.
   */
  UserList getLikingUsers(String tweetId, int maxResults);

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
   * @param additionalParameters accepted parameters are recursiveCall, maxResults, paginationToken
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

  /**
   * Allows an authenticated user ID to mute the target user calling https://api.twitter.com/2/users/:id/muting
   *
   * @param userId The user ID of the user that you would like the id to mute.
   */
  UserActionResponse muteUser(String userId);

  /**
   * Allows an authenticated user ID to unmute the target user calling https://api.twitter.com/2/users/:source_user_id/muting/:target_user_id. The
   * request succeeds with no action when the user sends a request to a user they're not muting or have already unmuted.
   *
   * @param userId The user ID of the user that you would like the id to mute.
   */
  UserActionResponse unmuteUser(String userId);

  /*
  Returns a list of users who are muted by the specified user ID calling https://api.twitter.com/2/users/:id/muting
   */
  UserList getMutedUsers();

  /**
   * Allows you to get information about who has Retweeted a Tweet calling https://api.twitter.com/2/tweets/:id/retweeted_by
   *
   * @param tweetId the id of the tweet
   * @param maxResults the maximum results to be returned.
   */
  UserList getRetweetingUsers(String tweetId, int maxResults);

  /**
   * Allows you to get information about all users who have Retweeted a Tweet calling https://api.twitter.com/2/tweets/:id/retweeted_by
   *
   * @param tweetId the id of the tweet
   */
  UserList getRetweetingUsers(String tweetId);

  /**
   * Causes the user identified to Retweet the target Tweet calling https://api.twitter.com/2/users/:id/retweets
   *
   * @param tweetId the id of the tweet
   */
  RetweetResponse retweetTweet(String tweetId);

  /**
   * Allows the user or authenticated user to remove the Retweet of a Tweet calling https://api.twitter.com/2/users/:id/retweets/:source_tweet_id
   *
   * @param tweetId the id of the tweet
   */
  RetweetResponse unretweetTweet(String tweetId);

  /**
   * Returns a variety of information about a single Space specified by the requested ID.
   *
   * @param spaceId Unique identifier of the Space to request.
   */
  Space getSpace(String spaceId);

  /**
   * Returns details about multiple Spaces. Up to 100 Spaces IDs can be looked up using this endpoint.
   */
  SpaceList getSpaces(List<String> spaceIds);

  /**
   * Returns live or scheduled Spaces created by the specified user IDs. Up to 100 IDs can be looked up using this endpoint.
   */
  SpaceList getSpacesByCreators(List<String> creatorIds);

  /**
   * Return live or scheduled Spaces matching your specified search terms
   *
   * @param query Your search term. This can be any text (including mentions and Hashtags) present in the title of the Space.
   * @param state Determines the type of results to return. Use LIVE to return live Spaces or SCHEDULED to return upcoming Spaces.
   */
  SpaceList searchSpaces(String query, SpaceState state);

  /**
   * Returns a list of user who purchased a ticket to the requested Space calling https://api.twitter.com/2/spaces/:id/buyers
   *
   * @param spaceId Unique identifier of the Space for which you want to request Tweets.
   */

  UserList getSpaceBuyers(String spaceId);

  /**
   * Enables the authenticated user to create a List calling https://api.twitter.com/2/lists
   *
   * @param listName The name of the List you wish to create.
   * @param description Description of the List.
   * @param isPrivate Determine whether the List should be private.
   */
  TwitterList createList(String listName, String description, boolean isPrivate);

  /**
   * Enables the authenticated user to delete a List that they own calling https://api.twitter.com/2/lists/:id
   *
   * @param listId The ID of the List to be deleted.
   * @return false if the list is deleted
   */
  boolean deleteList(String listId);

  /**
   * Enables the authenticated user to add a member to a List they own calling https://api.twitter.com/2/lists/:id/members
   *
   * @param listId The ID of the List you are adding a member to.
   * @param userId The ID of the user you wish to add as a member of the List.
   * @return true if the member is added
   */
  boolean addListMember(String listId, String userId);

  /**
   * Enables the authenticated user to remove a member from a List they own.
   *
   * @param listId The ID of the List you are removing a member from.
   * @param userId The ID of the user you wish to remove as a member of the List.
   * @return false if the member is removed
   */
  boolean removeListMember(String listId, String userId);

  /**
   * Enables the authenticated user to pin a List calling https://api.twitter.com/2/users/:id/pinned_lists
   *
   * @param listId The ID of the List that you would like the user id to pin.
   * @return true if the list is pinned
   */
  boolean pinList(String listId);

  /**
   * Enables the authenticated user to unpin a List calling https://api.twitter.com/2/users/:id/pinned_lists/:list_id
   *
   * @param listId The ID of the List that you would like the user id to unpin.
   * @return false if the list is unpinned
   */
  boolean unpinList(String listId);

  /**
   * Enables the authenticated user to update the meta data of a specified List that they own calling https://api.twitter.com/2/lists/:id
   *
   * @param listId The ID of the List to be updated.
   * @param description Updates the description of the List.
   * @param listName Updates the name of the List.
   * @param isPrivate Determines whether the List should be private.
   * @return true if the list was updated
   */
  boolean updateList(String listId, String listName, String description, boolean isPrivate);

  /**
   * Enables the authenticated user to follow a List calling /2/users/:id/followed_lists
   *
   * @param listId The ID of the List that you would like the user id to follow.
   * @return the following status
   */
  boolean followList(String listId);

  /**
   * Enables the authenticated user to unfollow a List calling https://api.twitter.com/2/users/:id/followed_lists/:list_id
   *
   * @param listId The ID of the List that you would like the user id to unfollow.
   * @return the following status
   */
  boolean unfollowList(String listId);

  /**
   * Returns the details of a specified List calling https://api.twitter.com/2/lists/:id
   *
   * @param listId The ID of the List to lookup.
   */
  TwitterList getList(String listId);

  /**
   * Get a tweet list by list id calling https://api.twitter.com/2/tweets
   *
   * @param listId The ID of the List to lookup.
   * @param additionalParameters accepted parameters are recursiveCall, sinceId, maxResults*
   */
  TweetList getListTweets(String listId, AdditionalParameters additionalParameters);

  /**
   * Returns a list of users who are members of the specified List
   *
   * @param listId The ID of the List whose members you would like to retrieve.
   */
  UserList getListMembers(String listId);

  /**
   * Returns all Lists owned by the specified user calling https://api.twitter.com/2/users/:id/owned_lists
   *
   * @param userId The user ID whose owned Lists you would like to retrieve.
   */
  TwitterListList getUserOwnedLists(String userId);

  /**
   * Creates a Tweet on behalf of an authenticated user calling https://api.twitter.com/2/tweets
   *
   * @param text Text of the Tweet being created.
   * @return the created tweet, only with id and text fields
   */
  Tweet postTweet(String text);

  /**
   * Creates a Tweet on behalf of an authenticated user calling https://api.twitter.com/2/tweets
   *
   * @param tweetParameters object containing additional parameters. See https://developer.twitter.com/en/docs/twitter-api/tweets/manage-tweets/api-reference/post-tweets
   * @return the created tweet, only with id and text fields
   */
  Tweet postTweet(TweetParameters tweetParameters);

  /**
   * Allows a user or authenticated user ID to delete a Tweet calling https://api.twitter.com/2/tweets/:id
   *
   * @param tweetId The Tweet ID you are deleting.
   * @return true if the tweet was deleted
   */
  boolean deleteTweet(String tweetId);


}