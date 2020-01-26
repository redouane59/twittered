package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserStatsDTO {
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("following_count")
    private int followingCount;
    @JsonProperty("tweet_count")
    private int tweetCount;
    @JsonProperty("listed_count")
    private int listedCount;
}
