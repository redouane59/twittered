package com.socialmediaraiser.core.twitter.helpers.dto.user;

import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDTOv2;
import lombok.Data;

import java.util.List;

@Data
public class IncludesDTO {
    private List<TweetDTOv2> tweets;
}
