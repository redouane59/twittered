package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import lombok.Data;

import java.util.Date;
import java.util.logging.Logger;

@Data
public class UserDTO extends AbstractUser {
    private static final Logger LOGGER = Logger.getLogger(UserDTO.class.getName());

    @JsonProperty("created_at")
    private String createdAt;
    private String name;
    private String location;
    private String url;
    private boolean verified;
    private JsonNode entities;
    @JsonProperty("profile_image_url")
    private String profileImageUrl;
    private UserStatsDTO stats;
    @JsonProperty("most_recent_tweet_id")
    private String mostRecentTweetId;
    @JsonProperty("pinned_tweet_id")
    private String pinnedTweetId;
    private String format;

    @Override
    public Date getDateOfCreation() {
        return JsonHelper.getDateFromTwitterDateV2(this.createdAt);
    }

    @Override
    public Date getLastUpdate() {
        if(this.getMostRecentTweet()==null && !this.getMostRecentTweet().isEmpty()){
            LOGGER.severe(()->"mostRecentTweet null");
            return null;
        }
        return JsonHelper.getDateFromTwitterDateV2(this.getMostRecentTweet().get(0).getCreatedAt());
    }

    @Override
    public int getFollowersCount() {
        return this.stats.getFollowersCount();
    }

    @Override
    public int getFollowingCount() {
        return this.stats.getFollowingCount();
    }

    @Override
    public int getTweetCount() {
        return this.stats.getTweetCount();
    }

    @Override
    public String getLang() {
        if(this.getMostRecentTweet()==null && !this.getMostRecentTweet().isEmpty()){
            LOGGER.severe(()->"mostRecentTweet null");
            return null;
        }
        return this.getMostRecentTweet().get(0).getLang();
    }
}
