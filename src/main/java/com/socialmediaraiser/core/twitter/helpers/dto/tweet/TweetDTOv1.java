package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.user.UserDTOv1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @version current
 */

@Builder
@Data
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
        return JsonHelper.getDateFromTwitterString(this.createdAt);
    }

}
