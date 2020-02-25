package com.socialmediaraiser.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicMetrics {
    @JsonProperty("followers_count")
    private int followersCount;
    @JsonProperty("following_count")
    private int followingCount;
    @JsonProperty("tweet_count")
    private int tweetCount;
    @JsonProperty("listed_count")
    private int listedCount;
}
