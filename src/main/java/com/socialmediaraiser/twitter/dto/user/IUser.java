package com.socialmediaraiser.twitter.dto.user;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import java.util.Date;
import java.util.List;

public interface IUser {

    /**
     * Get the string id of the user
     * @return the user id
     */
    String getId();

    /**
     * Get the screen name of the user
     * @return the user name
     */
    String getName();

    /**
     * Get the location of the user
     * @return the user location
     */
    String getLocation();

    /**
     * Get the bio of a user
     * @return the user description
     */
    String getDescription();

    /**
     * Get the date of creation of the user account
     * @return the user account date of creation
     */
    Date getDateOfCreation();

    /**
     * Get the number of followers of the user
     * @return the user number of followers
     */
    int getFollowersCount();

    /**
     * Get the number of followings of the user
     * @return the user number of followings
     */
    int getFollowingCount();

    /**
     * Get the number of tweets written by the user
     * @return the user total number of tweets
     */
    int getTweetCount();

    /**
     * Get the language of the user
     * @return the user language
     */
    String getLang();

    /**
     * Get the protection level of the user account
     * @return true if the account is protected, else false
     */
    boolean isProtectedAccount();

    /**
     * Know if the user is following the owner account. Warning: this is not not support by all endpoints.
     * @return true if the user is following the owner account, else false
     */
    boolean isFollowing();

}
