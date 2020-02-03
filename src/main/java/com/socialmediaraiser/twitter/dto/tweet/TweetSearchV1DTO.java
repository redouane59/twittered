package com.socialmediaraiser.twitter.dto.tweet;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TweetSearchV1DTO {
    private List<TweetDTOv1> results;
    private String next;
}
