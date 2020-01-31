package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import com.socialmediaraiser.core.twitter.IUser;

import java.util.Date;

public interface ITweet {

    String getId();
    String getText();
    int getRetweetCount();
    int getLikeCount();
    int getReplyCount();
    Date getCreatedAt();
    String getInReplyToUserId();
    String getInReplyToStatusId();
    String getLang();
    IUser getUser();

}
