package com.socialmediaraiser.twitter;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;

import java.util.Date;
import java.util.List;

public interface IUser {

    String getId();
    String getName();
    String getLocation();
    String getDescription();
    Date getDateOfCreation();
    Date getLastUpdate();
    int getFollowersCount();
    int getFollowingCount();
    int getTweetCount();
    String getLang();
    List<ITweet> getMostRecentTweet();
    boolean isProtectedAccount();

}
