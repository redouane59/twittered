package com.socialmediaraiser.twitter.dto.getrelationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListDTO {
    private List<UserDTOv1> users;
    @JsonProperty("next_cursor_str")
    private String nextCursor;
    @JsonProperty("previous_cursor_str")
    private String previousCursorStr;
}
