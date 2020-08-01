package com.github.redouane59.twitter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv2;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import lombok.*;
import java.util.Date;

/**
 * @version labs
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CustomLog
public class UserDTOv2 implements IUser {

    private UserData data;
    private UserData.Includes includes;

    @Getter
    @Setter
    public static class UserData implements IUser{
        private String id;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("username")
        private String name;
        private String location;
        private String url;
        private boolean verified;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
        @JsonProperty("public_metrics")
        private UserPublicMetrics publicMetrics;
        @JsonProperty("pinned_tweet_id")
        private String pinnedTweetId;
        private String description;
        private String lang;
        private boolean isProtectedAccount;
        private boolean following;

        @Getter
        @Setter
        public static class Includes{
            private TweetDTOv2.TweetData[] tweets;
        }

        @Override
        public Date getDateOfCreation() {
            return ConverterHelper.getDateFromTwitterString(this.createdAt);
        }

        @Override
        public int getFollowersCount() {
            return this.publicMetrics.getFollowersCount();
        }

        @Override
        public int getFollowingCount() {
            return this.publicMetrics.getFollowingCount();
        }

        @Override
        public int getTweetCount() {
            return this.publicMetrics.getTweetCount();
        }

        @Override
        public ITweet getPinnedTweet(){
            LOGGER.severe("Enable to access the data from here");
            return null;
        }
    }

    @Override
    public String getId() {
        return this.data.getId();
    }

    @Override
    public String getName() {
        return this.data.getName();
    }

    @Override
    public String getLocation() {
        return this.data.getLocation();
    }

    @Override
    public String getDescription() {
        return this.data.getDescription();
    }

    @Override
    public Date getDateOfCreation() {
        return ConverterHelper.getDateFromTwitterDateV2(this.data.getCreatedAt());
    }

    @Override
    public int getFollowersCount() {
        return this.data.getPublicMetrics().getFollowersCount();
    }

    @Override
    public int getFollowingCount() {
        return this.data.getPublicMetrics().getFollowingCount();
    }

    @Override
    public int getTweetCount() {
        return this.data.getPublicMetrics().getTweetCount();
    }

    @Override
    public String getLang() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isProtectedAccount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFollowing() {
        return this.data.isFollowing();
    }

    @Override
    public ITweet getPinnedTweet(){
        if(this.includes.getTweets().length<1){
            LOGGER.severe("No tweet found");
            return null;
        }
        return this.includes.getTweets()[0];
    }
}
