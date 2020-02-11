package com.socialmediaraiser.twitter.dto.tweet;

import com.socialmediaraiser.twitter.IUser;
import java.util.Date;

public interface ITweet {
    String getId();
    String getText();
    int getRetweetCount();
    int getLikeCount();
    int getReplyCount();
    int getQuoteCount();
    Date getCreatedAt();
    String getInReplyToUserId();
    String getInReplyToStatusId();
    String getLang();
    IUser getUser();
    String getAuthorId();

}
