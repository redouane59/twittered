package com.socialmediaraiser.core.twitter.helpers.dto;

import java.util.Date;

public interface IUser {

    String getId();
    String getUsername();
    String getLocation();
    String getDescription();
    Date getDateOfCreation();
    Date getLastUpdate();
    int getFollowersCount();
    int getFollowingCount();
    int getTweetCount();
    String getLang();
    boolean isProtectedAccount();

}
