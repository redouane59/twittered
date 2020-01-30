package com.socialmediaraiser.core.twitter.helpers.dto.tweet;

import lombok.Data;

import java.util.List;

@Data
public class DescriptionDTO {
    private List<HashtagDTO> hashtags;
    private List<MentionDTO> mentions;
}
