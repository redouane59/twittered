package io.github.redouane59.twitter;

import io.github.redouane59.RelationType;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse;
import io.github.redouane59.twitter.dto.collections.CollectionsResponse.Response.Position;
import io.github.redouane59.twitter.dto.collections.TimeLineOrder;
import io.github.redouane59.twitter.dto.dm.DirectMessage;
import io.github.redouane59.twitter.dto.dm.DmEvent;
import io.github.redouane59.twitter.dto.others.RateLimitStatus;
import io.github.redouane59.twitter.dto.others.RequestToken;
import io.github.redouane59.twitter.dto.tweet.MediaCategory;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.UploadMediaResponse;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public interface ITwitterClientV1 {

  /**
   * Returns a cursored collection of user IDs for every user following the specified user calling https://api.twitter.com/1.1/followers/ids.json
   *
   * @Deprecated <p> Use {@link ITwitterClientV2#getFollowers(String)} instead.
   */
  @Deprecated
  List<String> getFollowersIds(String userId);

  /**
   * Returns a cursored collection of user IDs for every user the specified user is following (otherwise known as their "friends") calling
   * https://api.twitter.com/1.1/friends/ids.json
   *
   * @Deprecated <p> Use {@link ITwitterClientV2#getFollowing(String)} instead.
   */
  @Deprecated
  List<String> getFollowingIds(String userId);

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
   * Get the twitter rate limit status calling https://api.twitter.com/1.1/application/
   *
   * @return the twitter response
   */
  RateLimitStatus getRateLimitStatus();

  /**
   * Search tweets from last 30 days calling https://api.twitter.com/1.1/tweets/search/30day/dev.json Your development environment name should be
   * "dev". See https://developer.twitter.com/en/account/environments
   *
   * @param query the search query
   * @param fromDate the start date
   * @param toDate the end date
   * @param envName name of the premium environment. See https://developer.twitter.com/en/account/environments
   * @return a list of tweets
   * @Deprecated <p> Use {@link ITwitterClientV2#searchTweets(String)} instead.
   */
  @Deprecated
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
   * @Deprecated <p> Use {@link ITwitterClientV2#searchAllTweets(String)} instead.
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
  UploadMediaResponse uploadMedia(String mediaName, byte[] data, MediaCategory mediaCategory);

  /**
   * Upload a media calling https://upload.twitter.com/1.1/media/upload.json
   */
  UploadMediaResponse uploadMedia(File media, MediaCategory mediaCategory);

  /**
   * Creates a collection of tweets. See https://api.twitter.com/1.1/collections/create.json
   *
   * @param name required. Name of the collection - truncated by twitter to 25 characters
   * @param description optional. Description for collection - truncated by twitter to 160 characters
   * @param collectionUrl optional. A fully-qualified URL to associate with this collection.
   * @param timeLineOrder optional. Order Tweets chronologically or in the order they are added to a Collection. Defaults to order tweets added to
   * collection ({@link TimeLineOrder#CURATION_ORDER})
   * @return CollectionCreateResponse - response from https://api.twitter.com/1.1/collections/create.json - including the collection identifier
   * 'timeline_id'
   */
  CollectionsResponse collectionsCreate(String name, String description, String collectionUrl, TimeLineOrder timeLineOrder);

  /**
   * Adds tweets to an existing collection. See https://api.twitter.com/1.1/collections/create.json
   *
   * @param collectionId the id of the collection to add tweets to
   * @param tweetIds Tweets to be added to collection.  > 100 will result in multiple calls to the collections/create.json endpoint
   * @return an object indicating either no errors or listing the errors for each tweet that was rejected
   */
  CollectionsResponse collectionsCurate(String collectionId, List<String> tweetIds);

  /**
   * Gets tweets from an existing collection. See https://api.twitter.com/1.1/collections/entries.json
   *
   * To retrieve Tweets further back in time, use the value of min_position found in the current response as the max_position parameter in the next
   * call to this endpoint.
   *
   * @param collectionId the id of the collection to retrieve tweets from
   * @param count optional. Specifies the maximum number of results to include in the response 1-200.  {@link Position#getWasTruncated()} will
   * indicate if more tweets in collection
   * @param maxPosition optional. Returns results with a position value less than or equal to the specified position (tweetId in collection)
   * @param minPosition optional. Returns results with a position greater than the specified position (tweetId in collection)
   */
  CollectionsResponse collectionsEntries(String collectionId, int count, String maxPosition, String minPosition);

  /**
   * Destroys a collection by id. See https://api.twitter.com/1.1/collections/destroy.json
   *
   * @param collectionId the identifier of the collection to destroy
   * @return {@link CollectionsResponse#isDestroyed()}
   */
  CollectionsResponse collectionsDestroy(String collectionId);

  /**
   * Returns all Direct Message events (both sent and received) within the last 30 days. Sorted in reverse-chronological order. Calling
   * https://api.twitter.com/1.1/direct_messages/events/list.json
   */
  List<DirectMessage> getDmList();

  /**
   * Returns all Direct Message events (both sent and received) within the last 30 days. Sorted in reverse-chronological order. Calling
   * https://api.twitter.com/1.1/direct_messages/events/list.json
   *
   * @param count Number of direct messages to be returned
   */
  List<DirectMessage> getDmList(int count);

  /**
   * Returns a single Direct Message event by the given id.
   *
   * @param dmId The id of the Direct Message event that should be returned.
   */
  DirectMessage getDm(String dmId);

  /**
   * Publishes a new message_create event resulting in a Direct Message sent to a specified user from the authenticating user
   *
   * @param text defining the content to deliver to the reciepient
   * @param userId The ID of the user who should receive the direct message.
   */
  DmEvent postDm(String text, String userId);

}