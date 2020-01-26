package com.socialmediaraiser.core.twitter.properties;

import lombok.Data;

@Data
public class InfluencerProperties {
    private float minRatio;
    private int minNbFollowers;
    private String baseList;
}
