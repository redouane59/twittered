package com.socialmediaraiser.core.twitter;

import com.socialmediaraiser.core.RelationType;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.ITweet;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDataDTO;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface ITwitterClient {


    /**
     * Get a list of the user followers
     * @param userId the id of the targeted user
     * @return a list of AbstractUser who are following the targeted user
     */
    List<IUser> getFollowerUsers(String userId);

    /**
     * Get a list of the user followers ids
     * @param userId the id of the targeted user
     * @return a list of ids of users who are following the targeted user
     */
    List<String> getFollowerIds(String userId);

    /**
     * Get a list of the user followings
     * @param userId the id of the targeted user
     * @return a list of AbstractUser that the targeted user is following
     */
    List<IUser> getFollowingsUsers(String userId);

    /**
     * Get a list of the user followings ids
     * @param userId the id of the targeted user
     * @return a list of ids of users that the targeted user is following
     */
    List<String> getFollowingIds(String userId);

    /**
     * Get the follower ids list of the targeted user
     * @param userId the id of the targeted user
     * @return a list of ids of users who are following the targeted user
     */
    Set<String> getUserFollowersIds(String userId);


    /**
     * Like a tweet
     * @param tweetId the id of the tweet
     */
    void likeTweet(String tweetId);

    /**
     * Retweet a tweet
     * @param tweetId the id of the tweet
     */
    void retweetTweet(String tweetId);

    /**
     * Get a list of ids of the users who retweeted a tweet
     * @param tweetId the id of the tweet
     * @return a list of the ids of the users who retweeted a tweet
     */
    List<String> getRetweetersId(String tweetId);

    /**
     * Get the relation between two users
     * @param userId1 id of the first user
     * @param userId2 id of the second user
     * @return FRIENDS if the two users are following each other,
     * NONE if they neither of the two is following the other,
     * FOLLOWER if user2 follows user1,
     * FOLLOWING if user1 follows user2
     */
    RelationType getRelationType(String userId1, String userId2);

    /**
     * Retreive a user from his screen name
     * @param userName the name of the targeted user
     * @return an AbstractUser object related to the targeted user
     */
    IUser getUserFromUserName(String userName);
    /**
     * Retreive a user from his id
     * @param userId the id of the user
     * @return an AbstractUser object related to the targeted user
     */
    IUser getUserFromUserId(String userId);

    /**
     * Follow a user
     * @param userId the id of the user
     * @return true if the action works
     */
    boolean follow(String userId);

    /**
     * Unfollow a user
     * @param userId the id of the user
     * @return true if the action works
     */
    boolean unfollow(String userId);

    /**
     * Unfollow a user
     * @param userName the scree name of the user
     * @return true if the action works
     */
    boolean unfollowByName(String userName);

    /**
     * Get the twitter rate limit status
     * @return the twitter response
     */
    String getRateLimitStatus();

    List<TweetDataDTO> readTwitterDataFile(File file) throws IOException;
    /**
     * Get the last tweets of a user
     * @param userId the id of the user
     * @param count the number of tweets
     * @return a list of the user last tweets
     */
    List<ITweet> getUserLastTweets(String userId, int count);

    /**
     * Search historic tweets
     * @param query the search query
     * @param fromDate the start date
     * @param toDate the end date
     * @return a list of tweets
     */
    List<ITweet> searchForTweets(String query, Date fromDate, Date toDate);
}

