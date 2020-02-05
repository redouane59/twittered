package com.socialmediaraiser.twitter;

import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.dto.others.RateLimitStatusDTO;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDataDTO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface ITwitterClient {

    /**
     * Get a list of the user followers calling https://api.twitter.com/1.1/followers
     * @param userId the id of the targeted user
     * @return a list of AbstractUser who are following the targeted user
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
     * @return a list of AbstractUser that the targeted user is following
     */
    List<IUser> getFollowingsUsers(String userId);

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
     * @return FRIENDS if the two users are following each other,
     * NONE if they neither of the two is following the other,
     * FOLLOWER if user2 follows user1,
     * FOLLOWING if user1 follows user2
     */
    RelationType getRelationType(String userId1, String userId2);

    /**
     * Retreive a user from his screen name calling https://api.twitter.com/labs/1/users/
     * @param userName the name of the targeted user
     * @return an AbstractUser object related to the targeted user
     */
    IUser getUserFromUserName(String userName);

    /**
     * Retreive a user from his id calling https://api.twitter.com/labs/1/users/
     * @param userId the id of the user
     * @return an AbstractUser object related to the targeted user
     */
    IUser getUserFromUserId(String userId);

    /**
     * Follow a user calling https://api.twitter.com/1.1/friendships/
     * @param userId the id of the user
     * @return true if the action works
     */
    boolean follow(String userId);

    /**
     * Unfollow a user calling https://api.twitter.com/1.1/friendships/
     * @param userId the id of the user
     * @return true if the action works
     */
    boolean unfollow(String userId);

    /**
     * Unfollow a user calling https://api.twitter.com/1.1/friendships/
     * @param userName the scree name of the user
     * @return true if the action works
     */
    boolean unfollowByName(String userName);

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
     * Search historic tweets calling https://api.twitter.com/1.1/tweets/search/30day/
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsWithin30days(String query, Date fromDate, Date toDate);

    /**
     * Search historic tweets calling https://api.twitter.com/labs/1/tweets/search
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweetsWithin7days(String query, Date fromDate, Date toDate);

    /**
     * Parse the Twitter extract data archive file in a List of Tweets
     * @param file the downloaded file on https://twitter.com/settings/your_twitter_data converted in .json format
     * @return the list of tweets
     * @throws IOException
     */
    List<TweetDataDTO> readTwitterDataFile(File file) throws IOException;
}

