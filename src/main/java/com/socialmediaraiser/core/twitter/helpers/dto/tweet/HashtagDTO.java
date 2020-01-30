package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import lombok.Data;

@Data
public class HashtagDTO {
    private int start;
    private int end;
    private String tag;
}
