package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import lombok.Data;

@Data
public class MentionDTO {
    private int start;
    private int end;
    private String username;
}
