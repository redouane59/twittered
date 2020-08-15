package com.github.redouane59.twitter;

import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.dto.others.RateLimitStatusDTO;
import com.github.redouane59.twitter.dto.others.RequestTokenDTO;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv1;
import com.github.redouane59.twitter.dto.user.IUser;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ITwitterClient {

    /**
     * Get a list of the user followers calling https://api.twitter.com/1.1/followers
     * @param userId the id of the targeted user
     * @return a list of users who are following the targeted user
     */
    List<IUser> getFollowerUsers(String userId);

    /**
     * Get a list of the user followers ids calling https://api.twitter.com/1.1/followers
     * @param userId the id of the targeted user
     * @return a list of ids of users who are following the targeted user
     */
    List<String> getFollowerIds(String userId);

    /**
     * Get a list of the user followings calling https://api.twitter.com/1.1/friends
     * @param userId the id of the targeted user
     * @return a list of users that the targeted user is following
     */
    List<IUser> getFollowingUsers(String userId);

    /**
     * Get a list of the user followings ids calling https://api.twitter.com/1.1/friends
     * @param userId the id of the targeted user
     * @return a list of ids of users that the targeted user is following
     */
    List<String> getFollowingIds(String userId);

    /**
     * Like a tweet calling https://api.twitter.com/1.1/favorites/
     * @param tweetId the id of the tweet
     */
    void likeTweet(String tweetId);

    /**
     * Retweet a tweet
     * @param tweetId the id of the tweet
     */
    void retweetTweet(String tweetId);

    /**
     * Get a list of ids of the users who retweeted a tweet calling https://api.twitter.com/1.1/statuses/retweeters/
     * @param tweetId the id of the tweet
     * @return a list of the ids of the users who retweeted a tweet
     */
    List<String> getRetweetersId(String tweetId);

    /**
     * Get the relation between two users calling https://api.twitter.com/1.1/friendships/
     * @param userId1 id of the first user
     * @param userId2 id of the second user
     * @return One of the following RelationType enum value :
     * FRIENDS if the two users are following each other,
     * NONE if they neither of the two is following the other,
     * FOLLOWER if user2 follows user1,
     * FOLLOWING if user1 follows user2
     */
    RelationType getRelationType(String userId1, String userId2);

    /**
     * Retreive a user from his screen name calling https://api.twitter.com/2/users/
     * @param userName the name of the targeted user
     * @return an user object related to the targeted user
     */
    IUser getUserFromUserName(String userName);

    /**
     * Retreive a user from his id calling https://api.twitter.com/2/users/
     * @param userId the id of the user
     * @return an user object related to the targeted user
     */
    IUser getUserFromUserId(String userId);

    /**
     * Retreive a list of users from their usernames calling https://api.twitter.com/2/users/
     * @param userNames the names of the targeted user
     * @return an list of user objects related to the targeted users
     */
    List<IUser> getUsersFromUserNames(List<String> userNames);

    /**
     * Retreive a list of users from their ids calling https://api.twitter.com/2/users/
     * @param userIds the id of the user
     * @return an list of user object related to the targeted users
     */
    List<IUser> getUsersFromUserIds(List<String> userIds);

    /**
     * Follow a user calling https://api.twitter.com/1.1/friendships/
     * @param userId the id of the user
     */
    void follow(String userId);

    /**
     * Unfollow a user calling https://api.twitter.com/1.1/friendships/
     * @param userId the id of the user
     */
    void unfollow(String userId);

    /**
     * Unfollow a user calling https://api.twitter.com/1.1/friendships/
     * @param userName the scree name of the user
     */
    void unfollowByName(String userName);

    /**
     * Get the twitter rate limit status calling https://api.twitter.com/1.1/application/
     * @return the twitter response
     */
    RateLimitStatusDTO getRateLimitStatus();

    /**
     * Get the last tweets of a user calling https://api.twitter.com/1.1/statuses/
     * @param userId the id of the user
     * @param count the number of tweets
     * @return a list of the user last tweets
     */
    List<ITweet> getUserLastTweets(String userId, int count);

    /**
     * Get a tweet from its id calling https://api.twitter.com/2/tweets
     * @param tweetId id of the tweet
     * @return a tweet object
     */
    ITweet getTweet(String tweetId);

    /**
     * Get a tweet list from their id calling https://api.twitter.com/2/tweets
     * @param tweetIds the ids of the tweets
     * @return a tweet object list
     */
    List<ITweet> getTweets(List<String> tweetIds);

    /**
     * Get the most recent Tweets liked
     * @param userId id of the user
     * @param count number of needed tweets
     * @return a list of liked tweets
     */
    List<ITweet> getFavorites(String userId, int count);

    /**
     * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsWithin7days(String query, Date fromDate, Date toDate);

    /**
     * Search tweets from last 7 days calling https://api.twitter.com/2/tweets/search
     * @param query the search query
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsWithin7days(String query);

    /**
     * Search tweets from last 30 days calling https://api.twitter.com/1.1/tweets/search/30day/dev.json
     * Your development environment name should be "dev". See https://developer.twitter.com/en/account/environments
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsWithin30days(String query, Date fromDate, Date toDate);

    /**
     * Search historic tweets calling https://api.twitter.com/1.1/tweets/search/fullarchive/dev.json
     * Your development environment name should be "dev". See https://developer.twitter.com/en/account/environments
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsArchive(String query, Date fromDate, Date toDate);

    /**
     * Parse the Twitter extract data archive file in a List of Tweets
     * @param file the downloaded file on https://twitter.com/settings/your_twitter_data converted in .json format
     * @return the list of tweets
     * @throws IOException
     */
    List<TweetDTOv1> readTwitterDataFile(File file) throws IOException;

    /**
     * Get a bearer token (oAuth2) calling https://api.twitter.com/oauth2/token
     * @return the generated token
     */
    String getBearerToken();

    /**
     * Get token & secret token (oAuth1) calling https://api.twitter.com/oauth/request_token
     * @return and object containing the oauth token and the oauth token secret
     */
    RequestTokenDTO getOauth1Token();
}

