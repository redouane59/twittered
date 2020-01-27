package com.socialmediaraiser.core.twitter;

import com.socialmediaraiser.core.RelationType;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;

import java.util.List;
import java.util.Set;

public interface ITwitterClient {
    List<String> getRetweetersId(String tweetId);
    List<AbstractUser> getFollowerUsers(String userId);
    List<AbstractUser> getFollowingsUsers(String userId);
    Set<String> getUserFollowersIds(String userId);
    void likeTweet(String tweetId);
    void retweetTweet(String tweetId);
    List<Tweet> searchForTweets(String query, int count, String fromDate, String toDate, String url);
    RelationType getRelationType(String userId1, String userId2);
    List<String> getFollowerIds(String userId);
    List<String> getFollowingIds(String userId);
    AbstractUser getUserFromUserName(String userName);
    AbstractUser getUserFromUserId(String userId);
    boolean follow(String userId);
    boolean unfollow(String userId);
    boolean unfollowByName(String userName);
    String getRateLimitStatus();
    List<Tweet> getUserLastTweets(String userId, int count);
}

