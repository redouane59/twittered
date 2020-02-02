package com.socialmediaraiser.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmediaraiser.twitter.IUser;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * @version current
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTOv1 implements IUser {

    private static final Logger LOGGER = Logger.getLogger(UserDTOv1.class.getName());

    private String id;
    @JsonProperty("screen_name")
    @JsonAlias({"screen_name","username"})
    private String name;
    private List<ITweet> mostRecentTweet;
    private String description;
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

    public Date getDateOfCreation(){
        return ConverterHelper.getDateFromTwitterString(this.dateOfCreation);
    }

    public Date getLastUpdate(){
        return ConverterHelper.getDateFromTwitterString(this.lastUpdate);
    }

    public List<ITweet> getMostRecentTweet(){
        throw new UnsupportedOperationException();
    }
}
