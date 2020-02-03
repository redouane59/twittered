package com.socialmediaraiser.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import lombok.Data;

import java.util.List;

@Data
public class UserListDTO {
    private List<UserDTOv1> users;
    @JsonProperty("next_cursor_str")
    private String nextCursor;
    @JsonProperty("previous_cursor_str")
    private String previousCursorStr;
}
