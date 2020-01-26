package com.socialmediaraiser.core.twitter.properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TargetProperties {
    private int minNbFollowers;
    private int maxNbFollowers;
    private int minNbFollowings;
    private int maxNbFollowings;
    private String description;
    private String keywords;
    private String unwantedKeywords;
    private float minRatio;
    private float maxRatio;
    private int maxDaysSinceLastTweet;
    private int nbBaseFollowers;
    private String terms;
    private String location;
    private String language;
    private int minimumPercentMatch;
}
