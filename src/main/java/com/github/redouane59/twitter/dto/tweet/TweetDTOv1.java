package com.github.redouane59.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.dto.user.UserDTOv1;
import lombok.*;
import java.util.Date;
import java.util.List;

/**
 * @version current
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@CustomLog
public class TweetDTOv1 implements ITweet {
    private String id;
    private String lang;
    @JsonProperty("retweet_count")
    private int retweetCount;
    @JsonProperty("favorite_count")
    private int likeCount;
    @JsonProperty("reply_count")
    private int replyCount;
    @JsonProperty("quote_count")
    private int quoteCount;
    @JsonAlias({"text","full_text"})
    private String text;
    @JsonProperty("created_at")
    private String createdAt;
    private UserDTOv1 user;
    @JsonProperty("in_reply_to_status_id_str")
    private String inReplyToStatusId;
    @JsonProperty("in_reply_to_user_id_str")
    private String inReplyToUserId;
    @JsonProperty("is_quote_status")
    private boolean isQuoteStatus;

    public Date getCreatedAt(){
        return ConverterHelper.getDateFromTwitterString(this.createdAt);
    }

    @Override
    public List<ContextAnnotation> getContextAnnotations() {
        LOGGER.severe("not implemented");
        return null;
    }

    @Override
    public TweetType getTweetType() {
        if(this.isQuoteStatus) return TweetType.QUOTED;
        return null;
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Override
    public String getInReplyToStatusId(TweetType type) {
        return this.getInReplyToStatusId();
    }

    @Override
    public String getAuthorId() {
        if(this.user==null) return null;
        return this.user.getId();
    }

    public int hashCode() {
        return this.id.hashCode();
    }

}
