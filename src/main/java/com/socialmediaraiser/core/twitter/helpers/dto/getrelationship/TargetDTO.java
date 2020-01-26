package com.socialmediaraiser.core.twitter.helpers.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TargetDTO {
    @JsonProperty("followed_by")
    private boolean followedBy;
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("id_str")
    private String idStr;
    private boolean following;
    @JsonProperty("following_requested")
    private boolean followingRequested;
    private Long id;
    @JsonProperty("following_received")
    private boolean followingReceived;
}
