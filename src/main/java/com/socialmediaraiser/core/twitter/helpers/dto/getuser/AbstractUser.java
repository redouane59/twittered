package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.RandomForestAlgoritm;
import com.socialmediaraiser.core.twitter.scoring.Criterion;
import com.socialmediaraiser.core.twitter.scoring.UserScoringEngine;
import com.socialmediaraiser.core.twitter.helpers.dto.IUser;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
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
    private UserScoringEngine scoringEngine;
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

    public boolean getRandomForestPrediction(){
        this.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(this);
    }

    public boolean isLanguageOK(){
        return Option.of(this.getLang())
                .map(b -> this.getLang().equals(FollowProperties.getTargetProperties().getLanguage())).getOrElse(false);
    }

    public boolean isInfluencer(){
        String descriptionWords = FollowProperties.getTargetProperties().getDescription();
        String[] descriptionWordsSplitted = descriptionWords.split(FollowProperties.getArraySeparator());
        String[] userDescriptionSplitted = this.getDescription().split(" ");

        String locationWords = FollowProperties.getTargetProperties().getLocation();
        String[] locationWordsSplitted = Option.of(locationWords.split(FollowProperties.getArraySeparator())).getOrElse(new String[0]);

        String[] userLocationSplitted = this.getLocation().split(" ");

        boolean matchDescription = false;
        boolean matchLocation = false;

        for(String s :userDescriptionSplitted){
            if(Arrays.stream(descriptionWordsSplitted).anyMatch(s::contains)){
                matchDescription = true;
            }
        }
        for(String s :userLocationSplitted){
            if(Arrays.stream(locationWordsSplitted).anyMatch(s::contains)){
                matchLocation = true;
            }
        }
        return (matchDescription
                && matchLocation
                && this.getFollowersRatio() > FollowProperties.getInfluencerProperties().getMinRatio()
                && this.getFollowersCount()> FollowProperties.getInfluencerProperties().getMinNbFollowers());
    }

    public boolean shouldBeFollowed(String ownerName){
        if(this.getUsername()!=null && this.getUsername().equals(ownerName)){
            return false;
        }
        return this.scoringEngine.shouldBeFollowed(this);
    }

    public boolean shouldBeUnfollowed(Criterion criterion, int value){
        switch (criterion){
            case LAST_UPDATE:
                if(this.getLastUpdate()!=null) {
                    long nbDaysSinceLastUpdate = (new Date().getTime() - this.getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
                    if(nbDaysSinceLastUpdate>value){
                        return true;
                    }
                }
                return false;
            case RATIO:
                if(this.getFollowersRatio()<value){
                    return true;
                }
                return false;
            case NB_FOLLOWERS:
                if(this.getFollowersCount()<value){
                    return true;
                }
                return false;
            default:
                LOGGER.severe("Criterion " + criterion.name() + " not found");
                return false;
        }
    }

}
