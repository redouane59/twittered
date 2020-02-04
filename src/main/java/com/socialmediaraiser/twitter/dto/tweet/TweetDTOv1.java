package com.socialmediaraiser.twitter.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import lombok.*;

import java.util.Date;

/**
 * @version current
 */

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

    public Date getCreatedAt(){
        return ConverterHelper.getDateFromTwitterString(this.createdAt);
    }

}
