package com.socialmediaraiser.core.twitter.helpers.dto.getuser;

import lombok.Data;

import java.util.List;

@Data
public class DescriptionDTO {
    private List<HashtagDTO> hashtags;
    private List<MentionDTO> mentions;
}
