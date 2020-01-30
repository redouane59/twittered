package com.socialmediaraiser.core.twitter.helpers.dto.user;

import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDTO;
import lombok.Data;

import java.util.List;

@Data
public class IncludesDTO {
    private List<TweetDTO> tweets;
}
