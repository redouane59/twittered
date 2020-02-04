package com.socialmediaraiser.twitter.dto.tweet;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TweetSearchV1DTO {
    private List<TweetDTOv1> results;
    private String next;
}
