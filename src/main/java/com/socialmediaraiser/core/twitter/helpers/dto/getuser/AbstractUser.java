package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.socialmediaraiser.core.twitter.helpers.dto.IUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUser implements IUser {

    private static final Logger LOGGER = Logger.getLogger(AbstractUser.class.getName());

    private String id;
    private String username;
    private List<TweetDTO> mostRecentTweet;
    private String description;
    private Date dateOfFollow;
    @JsonAlias("protected")
    private boolean protectedAccount;
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollowBack;
   // private UserScoringEngine scoringEngine;
    private int nbInteractions;

    @Override
    public boolean equals(Object o) {
        if (o==null || this.getClass() != o.getClass()) return false;

        AbstractUser otherUser = (AbstractUser) o;
        return (otherUser).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    public double getFollowersRatio() {
        return (double) this.getFollowersCount() / (double) this.getFollowingCount();
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        if(dateOfFollow==null || this.getLastUpdate()==null){
            return Long.MAX_VALUE;
        }
        return (dateOfFollow.getTime()-this.getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
    }

    public long getYearsBetweenFollowAndCreation(){
        return (dateOfFollow.getTime()-this.getDateOfCreation().getTime()) / (365 * 24 * 60 * 60 * 1000);
    }
}
