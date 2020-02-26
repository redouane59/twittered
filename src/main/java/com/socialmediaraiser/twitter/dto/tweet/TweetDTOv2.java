package com.socialmediaraiser.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.user.UserDTOv2;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @version labs
 */
@Getter
@Setter
public class TweetDTOv2 implements ITweet {

    private TweetData data;
    private Includes includes;

    @Getter
    @Setter
    public static class TweetData implements ITweet {
        private String id;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonAlias({"text", "full_text"})
        private String text;
        @JsonProperty("author_id")
        private String authorId;
        @JsonProperty("in_reply_to_user_id")
        private String inReplyToUserId;
        @JsonProperty("referenced_tweets")
        private List<ReferencedTweetDTO> referencedTweets;
        private JsonNode entities;
        @JsonProperty("public_metrics")
        private TweetPublicMetricsDTO publicMetrics;
        @JsonProperty("possibly_sensitive")
        private boolean possiblySensitive;
        private String lang;

        @Override
        public int getRetweetCount() {
            return this.publicMetrics.getRetweetCount();
        }

        @Override
        public int getLikeCount() {
            return this.publicMetrics.getLikeCount();
        }

        @Override
        public int getReplyCount() {
            return this.publicMetrics.getReplyCount();
        }

        @Override
        public int getQuoteCount() {
            return this.publicMetrics.getQuoteCount();
        }

        @Override
        public String getInReplyToStatusId() {
            return this.referencedTweets.get(0).getId();
        }

        @Override
        public IUser getUser() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthorId(){
            return this.authorId;
        }

        @Override
        public Date getCreatedAt(){
            return ConverterHelper.getDateFromTwitterDateV2(this.createdAt);
        }

    }

    @Getter
    @Setter
    private static class Includes{
        private UserDTOv2.UserData[] users; // @TODO problem here
    }

    @Override
    public String getInReplyToStatusId(){
        if(this.data.getReferencedTweets().size()>0){
            return this.data.getReferencedTweets().get(0).getId();
        }
        return null;
    }

    @Override
    public String getLang() {
        return this.data.getLang();
    }

    @Override
    public String getId() {
        return this.data.getId();
    }

    @Override
    public String getText() {
        return this.data.getText();
    }

    @Override
    public int getRetweetCount() {
        return this.data.getPublicMetrics().getRetweetCount();
    }

    @Override
    public int getLikeCount() {
        return  this.data.getPublicMetrics().getLikeCount();
    }

    @Override
    public int getReplyCount() {
        return this.data.getPublicMetrics().getReplyCount();
    }

    @Override
    public int getQuoteCount() {
        return this.data.getPublicMetrics().getQuoteCount();
    }

    @Override
    public String getInReplyToUserId(){
        return this.data.getInReplyToUserId();
    }

    @Override
    public IUser getUser() {
        return this.includes.getUsers()[0];
    }

    @Override
    public String getAuthorId(){
        return this.data.getAuthorId();
    }

    @Getter
    @Setter
    private static class ReferencedTweetDTO {
        private String type;
        private String id;
    }

    @Getter
    @Setter
    private static class TweetPublicMetricsDTO {
        @JsonProperty("retweet_count")
        private int retweetCount;
        @JsonProperty("reply_count")
        private int replyCount;
        @JsonProperty("like_count")
        private int likeCount;
        @JsonProperty("quote_count")
        private int quoteCount;
    }

    public Date getCreatedAt(){
        return this.data.getCreatedAt();
    }
}
