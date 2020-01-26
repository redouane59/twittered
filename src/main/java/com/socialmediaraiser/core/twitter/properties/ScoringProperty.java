package com.socialmediaraiser.core.twitter.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.socialmediaraiser.core.twitter.scoring.Criterion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoringProperty {
    private static final Logger LOGGER = Logger.getLogger(ScoringProperty.class.getName());
    private Criterion criterion;
    private boolean active;
    private int maxPoints;
    private Object value;
    private boolean blocking;

    public void setCriterion(String s){
        switch (s){
            case "nbFollowers":
                this.criterion = Criterion.NB_FOLLOWERS;
                break;
            case "nbFollowings":
                this.criterion = Criterion.NB_FOLLOWINGS;
                break;
            case "description":
                this.criterion = Criterion.DESCRIPTION;
                break;
            case "location":
                this.criterion = Criterion.LOCATION;
                break;
            case "commonFollowers":
                this.criterion = Criterion.COMMON_FOLLOWERS;
                break;
            case "lastUpdate":
                this.criterion = Criterion.LAST_UPDATE;
                break;
            case "ratio":
                this.criterion = Criterion.RATIO;
                break;
            case "tweetCount":
                this.criterion = Criterion.NB_TWEETS;
                break;
            default:
                LOGGER.severe(()->"criterion " + s + " not found ");
                break;
        }
    }
}
