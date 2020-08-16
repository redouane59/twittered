package com.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import lombok.*;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version current
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CustomLog
public class UserDTOv1 implements IUser {

    private String id;
    @JsonProperty("screen_name")
    @JsonAlias({"screen_name","username"})
    private String       name;
    private List<ITweet> mostRecentTweet;
    private String       description;
    @JsonAlias("protected")
    private boolean protectedAccount;
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("friends_count")
    @JsonAlias({"friends_count","followings_count"})
    private int followingCount;
    private String lang;
    @JsonProperty("tweetCount")
    @JsonAlias({"statuses_count","tweets_count"})
    private int tweetCount;
    @JsonAlias("created_at")
    private String dateOfCreation;
    private String lastUpdate;
    private String location;
    private boolean following;

    @Override
    public boolean equals(Object o) {
        if (o==null || this.getClass() != o.getClass()) return false;
        UserDTOv1 otherUser = (UserDTOv1) o;
        return (otherUser).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

/*    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    } */

    public LocalDateTime getDateOfCreation(){
        return ConverterHelper.getDateFromTwitterString(this.dateOfCreation);
    }

    @Override
    public ITweet getPinnedTweet() {
        LOGGER.finer("UnsupportedOperation");
        return null;
    }

    public LocalDateTime getLastUpdate(){
        return ConverterHelper.getDateFromTwitterString(this.lastUpdate);
    }
}
