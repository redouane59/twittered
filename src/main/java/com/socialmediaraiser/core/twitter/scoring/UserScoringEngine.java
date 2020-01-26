package com.socialmediaraiser.core.twitter.scoring;

import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.properties.ScoringProperty;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import static com.socialmediaraiser.core.twitter.scoring.Criterion.NB_FOLLOWERS;

@Getter
public class UserScoringEngine {

    private static final Logger LOGGER = Logger.getLogger(UserScoringEngine.class.getName());
    private int limit;

    public UserScoringEngine(int minimumPercentMatch){
        if(minimumPercentMatch<=100 && minimumPercentMatch>=0){
            this.limit = FollowProperties.getScoringProperties().getTotalMaxPoints()*minimumPercentMatch/100;
        } else{
            LOGGER.severe(()->"argument should be between 0 & 100");
            this.limit = 100;
        }
    }

    public boolean shouldBeFollowed(AbstractUser user){
        int score = getUserScore(user);
        return score >= limit;
    }

    public int getUserScore(AbstractUser user){
        FollowProperties.getScoringProperties().getProperty(NB_FOLLOWERS).setValue(user.getFollowersCount());
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWINGS).setValue(user.getFollowingCount());
        FollowProperties.getScoringProperties().getProperty(Criterion.RATIO).setValue(user.getFollowersRatio());
        String description = user.getDescription();
        // @odo only if public account
        if(!user.isProtectedAccount() && user.getMostRecentTweet()!=null
                && !user.getMostRecentTweet().isEmpty()){ // adding the last tweet to description
            description = description.concat(user.getMostRecentTweet().get(0).getText());
        }
        FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).setValue(description);
        FollowProperties.getScoringProperties().getProperty(Criterion.LOCATION).setValue(user.getLocation());
        FollowProperties.getScoringProperties().getProperty(Criterion.COMMON_FOLLOWERS).setValue(user.getCommonFollowers());
        FollowProperties.getScoringProperties().getProperty(Criterion.NB_TWEETS).setValue(user.getTweetCount());
        FollowProperties.getScoringProperties().getProperty(Criterion.LAST_UPDATE).setValue(user.getLastUpdate());
        return this.computeScore();
    }

    private int computeScore(){
        int score = 0;
        for(ScoringProperty prop : FollowProperties.getScoringProperties().getProperties()){
            if(prop.isActive()) {
                if(prop.getValue()!=null) {
                    int modifValue = getModifValue(prop);
                    if (modifValue == 0 && prop.isBlocking()) {
                        return 0;
                    }
                    score += modifValue;
                } else if(prop.isBlocking()){
                    return 0;
                }
            }
        }
        return score;
    }

    private int getModifValue(ScoringProperty prop){
        switch (prop.getCriterion()) {
            case NB_FOLLOWERS:
                return getNbFollowersScore((int) prop.getValue());
            case NB_FOLLOWINGS:
                return getNbFollowingsScore((int) prop.getValue());
            case RATIO:
                return getRatioScore((double) prop.getValue());
            case LAST_UPDATE:
                return getLastUpdateScore((Date) prop.getValue());
            case DESCRIPTION:
                return getDescriptionScore(prop.getValue().toString());
            case LOCATION:
                return getLocationScore(prop.getValue().toString());
            case COMMON_FOLLOWERS:
                return getCommonFollowersScore((int) prop.getValue());
            default:
                LOGGER.severe(()->"no function found for " + prop.getCriterion());
                return 0;
        }
    }

    private int getCommonFollowersScore(int value) {
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.COMMON_FOLLOWERS).getMaxPoints();
        int maxFollow = FollowProperties.getTargetProperties().getNbBaseFollowers();
        return maxPoints*value/maxFollow;
    }

    private int getNbFollowersScore(int nbFollowers){
        int maxPoints = FollowProperties.getScoringProperties().getProperty(NB_FOLLOWERS).getMaxPoints();
        if(nbFollowers> FollowProperties.getTargetProperties().getMinNbFollowers()
                && nbFollowers< FollowProperties.getTargetProperties().getMaxNbFollowers()){
            return maxPoints;
        }
        return 0;
    }

    private int getNbFollowingsScore(int nbFollowings){
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.NB_FOLLOWINGS).getMaxPoints();
        if(nbFollowings> FollowProperties.getTargetProperties().getMinNbFollowings()
                && nbFollowings< FollowProperties.getTargetProperties().getMaxNbFollowings()){
            return maxPoints;
        }
        return 0;
    }

    private int getRatioScore(double ratio){
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.RATIO).getMaxPoints();
        if(ratio> FollowProperties.getTargetProperties().getMinRatio()
                && ratio< FollowProperties.getTargetProperties().getMaxRatio()){
            return maxPoints;
        }
        return 0;
    }

    private int getLastUpdateScore(Date lastUpdate){
        if(lastUpdate==null) {
            return 0;
        }
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.LAST_UPDATE).getMaxPoints();
        int maxDays = FollowProperties.getTargetProperties().getMaxDaysSinceLastTweet();
        if(maxDays<=0){
            maxDays=1;
        }
        Date now = new Date();
        int daysSinceLastUpdate = (int)((now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000));
        if(daysSinceLastUpdate < maxDays) {
            return maxPoints * (maxDays-daysSinceLastUpdate)/maxDays;
        } else{
            return 0;
        }
    }

    private int getDescriptionScore(String description){
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.DESCRIPTION).getMaxPoints();
        String[] words = FollowProperties.getTargetProperties().getDescription().split(FollowProperties.getArraySeparator());
        String[] descriptionSplitted = description.split(" ");
        for(String s :descriptionSplitted){
            if(Arrays.stream(words).anyMatch(s.toLowerCase()::contains)){
                LOGGER.fine(() -> " matches description with '" + s +"'");
                return maxPoints;
            }
        }
        return 0;
    }

    // @todo to test
    private int getLocationScore(String location){
        int maxPoints = FollowProperties.getScoringProperties().getProperty(Criterion.LOCATION).getMaxPoints();
        String targetLocation = FollowProperties.getTargetProperties().getLocation();
        if(targetLocation==null){
            return maxPoints;
        } else{
            String[] words = targetLocation.split(FollowProperties.getArraySeparator());
            if (Arrays.asList(words).contains(location)){
                return maxPoints;
            }
            return 0;
        }
    }
}
